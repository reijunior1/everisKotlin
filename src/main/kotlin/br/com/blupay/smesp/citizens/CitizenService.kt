package br.com.blupay.smesp.citizens

import br.com.blupay.blubasemodules.core.models.AuthCredentials
import br.com.blupay.blubasemodules.core.models.ResponseStatus.FAILED
import br.com.blupay.blubasemodules.core.models.ResponseStatus.PROCESSED
import br.com.blupay.blubasemodules.identity.people.PersonCreate
import br.com.blupay.blubasemodules.identity.people.PersonCredentials
import br.com.blupay.blubasemodules.shared.validations.enums.ValidationType
import br.com.blupay.smesp.core.drivers.EncoderManager
import br.com.blupay.smesp.core.providers.identity.IdentityProvider
import br.com.blupay.smesp.core.resources.citizens.exceptions.CitizenException
import br.com.blupay.smesp.core.resources.citizens.exceptions.CitizenNotFoundException
import br.com.blupay.smesp.core.resources.citizens.models.CitizenResponse
import br.com.blupay.smesp.core.resources.citizens.models.CitizenStatusResponse
import br.com.blupay.smesp.core.resources.shared.enums.OnboardFlow
import br.com.blupay.smesp.core.resources.shared.enums.OnboardFlow.VALIDATION
import br.com.blupay.smesp.core.resources.shared.enums.UserGroups
import br.com.blupay.smesp.core.resources.shared.models.PasswordRequest
import br.com.blupay.smesp.core.services.OwnerService
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
    private val walletRepository: WalletRepository
) {

    private val logger: Logger = LoggerFactory.getLogger(CitizenService::class.java)

    fun createCredentials(citizenId: UUID, request: PasswordRequest, auth: AuthCredentials): Mono<CitizenResponse> {
        logger.info("Creating a new user identity credentials from citizen")
        val citizen = findById(citizenId)

        if (OnboardFlow.CREDENTIALS != citizen.flow) {
            throw CitizenException("This citizen $citizenId already has a credential")
        }

        return identityProvider.createPerson(
            auth.token, PersonCreate.Request(
                id = citizen.id,
                name = citizen.name,
                register = citizen.cpf,
                email = citizen.email,
                phone = citizen.phone,
            )
        ).zipWhen {
            logger.info("Creating user password")
            identityProvider.createPersonCredentials(
                auth.token, citizenId, PersonCredentials.Request(
                    password = request.password,
                    groups = listOf("${UserGroups.CITIZENS}")
                )
            )
        }.map { tuple ->
            logger.info("Check if password is valid")
            val passwordCreated = tuple.t2
            if (!passwordCreated) throw CredentialException("Citizen $citizenId credentials were not created")
            tuple.t1
        }.map {
            val updatedCitizen = citizenRepository.save(citizen.copy(flow = VALIDATION))
            createCitizenCryptResponse(updatedCitizen)
        }
    }

    fun checkStatus(citizenId: UUID, auth: AuthCredentials, isOtherUser: Boolean = false): Mono<CitizenStatusResponse> {
        ownerService.userOwns(auth, citizenId)
        return verifyRules(auth.token)
    }

    fun findOne(citizenId: UUID, auth: AuthCredentials): CitizenResponse {
        logger.info("Finding citizen and checking credentials")

        ownerService.userOwns(auth, citizenId)

        val citizen = findById(citizenId)
        val wallet = walletRepository.findByOwner(citizenId)
        return createCitizenCryptResponse(citizen, wallet)
    }

    fun findOneByCpf(cpf: String, auth: AuthCredentials): CitizenResponse {
        logger.info("Finding citizen by CPF $cpf")
        val citizen = citizenRepository.findByCpf(cpf)
            ?: throw CitizenNotFoundException(cpf)

        return createCitizenCryptResponse(citizen)
    }

    fun findById(citizenId: UUID) = citizenRepository.findByIdOrNull(citizenId)
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
