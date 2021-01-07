package br.com.blupay.smesp.citizens

import br.com.blupay.blubasemodules.core.models.AuthCredentials
import br.com.blupay.blubasemodules.core.models.ResponseStatus.FAILED
import br.com.blupay.blubasemodules.core.models.ResponseStatus.PROCESSED
import br.com.blupay.blubasemodules.identity.people.PersonCredentials
import br.com.blupay.blubasemodules.identity.people.PersonSearch
import br.com.blupay.blubasemodules.shared.validations.enums.ValidationType
import br.com.blupay.smesp.core.drivers.EncoderManager
import br.com.blupay.smesp.core.providers.identity.IdentityProvider
import br.com.blupay.smesp.core.providers.token.wallet.IssueWallet
import br.com.blupay.smesp.core.resources.citizens.exceptions.CitizenException
import br.com.blupay.smesp.core.resources.citizens.exceptions.CitizenNotFoundException
import br.com.blupay.smesp.core.resources.citizens.models.CitizenResponse
import br.com.blupay.smesp.core.resources.citizens.models.CitizenStatusResponse
import br.com.blupay.smesp.core.resources.shared.enums.OnboardFlow
import br.com.blupay.smesp.core.resources.shared.enums.OnboardFlow.VALIDATION
import br.com.blupay.smesp.core.resources.shared.enums.UserGroups
import br.com.blupay.smesp.core.resources.shared.enums.UserTypes.CITIZEN
import br.com.blupay.smesp.core.resources.shared.models.PasswordRequest
import br.com.blupay.smesp.core.services.JwsService
import br.com.blupay.smesp.core.services.OwnerService
import br.com.blupay.smesp.token.Creditor
import br.com.blupay.smesp.token.TokenService
import br.com.blupay.smesp.token.TokenWalletService
import br.com.blupay.smesp.token.WalletData
import br.com.blupay.smesp.wallets.Wallet
import br.com.blupay.smesp.wallets.WalletRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.UUID
import javax.security.auth.login.CredentialException

@Service
class CitizenService(
    @Value("\${app.config.min-score}") private val minScore: Double,
    private val ownerService: OwnerService,
    private val citizenRepository: CitizenRepository,
    private val encoderManager: EncoderManager,
    private val identityProvider: IdentityProvider,
    private val tokenWalletService: TokenWalletService,
    private val walletRepository: WalletRepository,
    private val jwsService: JwsService,
    private val tokenService: TokenService
) {

    private val logger: Logger = LoggerFactory.getLogger(CitizenService::class.java)

    fun createCredentials(citizenId: UUID, request: PasswordRequest, auth: AuthCredentials): CitizenResponse {
        val citizen = findById(citizenId)

        if (OnboardFlow.CREDENTIALS != citizen.flow) {
            throw CitizenException("This citizen $citizenId already has a credential")
        }

        val credentialsCreated = identityProvider.createPersonCredentials(
            auth.token, citizenId, PersonCredentials.Request(
                password = request.password,
                groups = listOf("${UserGroups.CITIZENS}")
            )
        )

        if (!credentialsCreated) {
            throw CredentialException("Citizen $citizenId credentials were not created")
        }

        val pin = encoderManager.encrypt(request.password.substring(0, 4))
        val wallet = walletRepository.findByOwner(citizenId)
        if (wallet != null) {
            walletRepository.save(
                wallet.copy(
                    id = wallet.id,
                    owner = wallet.owner,
                    token = wallet.token,
                    type = wallet.type,
                    pin = pin
                )
            )
        }

        val updatedCitizen = citizenRepository.save(citizen.copy(flow = VALIDATION))
        return createCitizenCryptResponse(updatedCitizen)
    }

    fun hasPermissionToDoTransaction(token: String, citizenId: UUID): Boolean {
        return verifyRules(token, citizenId)
            .map { PROCESSED != it.status }
            .blockOptional()
            .orElse(false)
    }

    fun checkStatus(citizenId: UUID, auth: AuthCredentials, isOtherUser: Boolean = false): Mono<CitizenStatusResponse> {
        ownerService.userOwns(auth, citizenId)
        return verifyRules(auth.token)
    }

    fun findOne(citizenId: UUID, auth: AuthCredentials): CitizenResponse {
        ownerService.userOwns(auth, citizenId)

        val citizen = findById(citizenId)
        val wallet = walletRepository.findByOwner(citizenId)
        return createCitizenCryptResponse(citizen, wallet)
    }

    fun findOneByCpf(cpf: String, auth: AuthCredentials): CitizenResponse {
        val citizen = citizenRepository.findByCpf(cpf)
        if (citizen != null) {
            return createCitizenCryptResponse(citizen)
        }

        val personList = identityProvider.peopleSearch(auth.token, PersonSearch.Query(register = cpf))
        val person = personList.stream().findFirst().orElseThrow { throw CitizenNotFoundException(cpf) }
        val citizenSaved = citizenRepository.save(
            Citizen(
                person.id,
                person.name,
                person.register,
                person.email,
                person.phone,
                listOf()
            )
        )

        val pairKeys = jwsService.getKeyPairEncoded()
        val walletId = UUID.randomUUID()

        val wallet = tokenWalletService.issueWallet(
            auth.token,
            IssueWallet(citizenSaved.id.toString(), pairKeys.publicKey, Wallet.Role.PAYER)
        ).block()


        val walletSaved = walletRepository.save(
            Wallet(
                walletId,
                citizenSaved.id!!,
                wallet?.id!!,
                CITIZEN,
                Wallet.Role.PAYER,
                pairKeys.publicKey,
                pairKeys.privateKey,
                ""
            )
        )

        transferBenefit(citizenSaved.id, walletSaved)

        return createCitizenCryptResponse(citizenSaved)
    }

    fun transferBenefit(citizenId: UUID, wallet: Wallet) {
        val amount = getBenefit(citizenId)
        tokenService.moveToken(
            wallet.token.toString(),
            WalletData(wallet.id!!, wallet.privateKey, wallet.publicKey),
            wallet.token,
            listOf(Creditor(wallet.id, amount))
        )
    }

    fun findById(citizenId: UUID) = citizenRepository.findByIdOrNull(citizenId)
        ?: throw CitizenNotFoundException("$citizenId")

    private fun getBenefit(citizenId: UUID): Long {
        val citizen = findById(citizenId)
        return citizen.children.map { it.category.price }.sum()
    }

    private fun createCitizenCryptResponse(citizen: Citizen, wallet: Wallet? = null): CitizenResponse {
        return CitizenResponse(
            id = citizen.id!!,
            name = citizen.name,
            cpf = citizen.cpf,
            email = encoderManager.encrypt(citizen.email),
            phone = encoderManager.encrypt(citizen.phone),
            flow = citizen.flow,
            walletId = wallet?.id
        )
    }

    private fun verifyRules(token: String, fromCitizenId: UUID? = null): Mono<CitizenStatusResponse> {
        return identityProvider.verifyRules(token, listOf(ValidationType.SELFIE, ValidationType.COMPARE), fromCitizenId)
            .map { validationRules ->
                validationRules.validations
                    .filterNotNull()
                    .map {
                        if (PROCESSED == it.status) {
                            val status = if (it.score!! < minScore) FAILED else it.status
                            it.copy(status = status)
                        } else {
                            it
                        }
                    }.toList()
            }.map { validations ->
                val status = validations.reduce { acc, item -> if (PROCESSED != acc.status) acc else item }.status
                CitizenStatusResponse(status, validations)
            }
    }
}
