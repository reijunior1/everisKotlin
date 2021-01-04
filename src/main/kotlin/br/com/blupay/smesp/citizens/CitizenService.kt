package br.com.blupay.smesp.citizens

import br.com.blupay.blubasemodules.core.models.AuthCredentials
import br.com.blupay.blubasemodules.identity.people.PersonCredentials
import br.com.blupay.blubasemodules.identity.people.PersonSearch
import br.com.blupay.smesp.core.drivers.EncoderManager
import br.com.blupay.smesp.core.providers.identity.IdentityProvider
import br.com.blupay.smesp.core.providers.token.wallet.IssueWallet
import br.com.blupay.smesp.core.providers.token.wallet.WalletRole.PAYER
import br.com.blupay.smesp.core.resources.citizens.exceptions.CitizenException
import br.com.blupay.smesp.core.resources.citizens.exceptions.CitizenNotFoundException
import br.com.blupay.smesp.core.resources.citizens.models.CitizenResponse
import br.com.blupay.smesp.core.resources.shared.enums.OnboardFlow
import br.com.blupay.smesp.core.resources.shared.enums.OnboardFlow.VALIDATION
import br.com.blupay.smesp.core.resources.shared.enums.UserGroups
import br.com.blupay.smesp.core.resources.shared.enums.UserTypes.CITIZEN
import br.com.blupay.smesp.core.resources.shared.models.PasswordRequest
import br.com.blupay.smesp.core.services.JwsService
import br.com.blupay.smesp.core.services.OwnerService
import br.com.blupay.smesp.token.TokenWalletService
import br.com.blupay.smesp.wallets.Wallet
import br.com.blupay.smesp.wallets.WalletRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.UUID
import javax.security.auth.login.CredentialException

@Service
class CitizenService(
    private val ownerService: OwnerService,
    private val citizenRepository: CitizenRepository,
    private val encoderManager: EncoderManager,
    private val identityProvider: IdentityProvider,
    private val tokenWalletService: TokenWalletService,
    private val walletRepository: WalletRepository,
    private val jwsService: JwsService
) {

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

        val updatedCitizen = citizenRepository.save(citizen.copy(flow = VALIDATION))
        return createCitizenCryptResponse(updatedCitizen)
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
            IssueWallet(citizenSaved.id.toString(), pairKeys.publicKey.toString(), PAYER)
        ).block()
        walletRepository.save(
            Wallet(
                walletId,
                citizenSaved.id!!,
                wallet?.id!!,
                CITIZEN,
                pairKeys.publicKey,
                pairKeys.privateKey
            )
        )

        return createCitizenCryptResponse(citizenSaved)
    }

    fun getBenefit(citizenId: UUID): Long {
        val citizen = findById(citizenId)
        val children = citizen.children
        return children.map { it.category.price }.sum()
    }

    private fun findById(citizenId: UUID) = citizenRepository.findByIdOrNull(citizenId)
        ?: throw CitizenNotFoundException("$citizenId")

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
}
