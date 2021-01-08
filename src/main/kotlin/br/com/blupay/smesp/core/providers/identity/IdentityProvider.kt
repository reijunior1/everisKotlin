package br.com.blupay.smesp.core.providers.identity

import br.com.blupay.blubasemodules.identity.people.PersonCreate
import br.com.blupay.blubasemodules.identity.people.PersonCredentials
import br.com.blupay.blubasemodules.identity.validations.ValidationCheckStatus
import br.com.blupay.blubasemodules.shared.validations.enums.ValidationType
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.UUID

@Service
class IdentityProvider(
    private val identityClient: IdentityClient
) {
    private val logger: Logger = LoggerFactory.getLogger(IdentityProvider::class.java)

    fun createPerson(token: String, body: PersonCreate.Request): Mono<PersonCreate.Response> {
        logger.info("Creating a new person with id ${body.id}")
        return identityClient.createPerson(token, body)
            .onErrorResume { error ->
                Mono.error(IdentityException("Credentials could not be created. ${error.message}"))
            }
    }

    fun createPersonCredentials(token: String, personId: UUID, body: PersonCredentials.Request): Mono<Boolean> {
        logger.info("Creating credentials for person $personId, into groups ${body.groups}")
        return identityClient.createPersonCredentials(token, personId, body)
            .onErrorResume { error ->
                Mono.error(IdentityException("Credentials could not be created. ${error.message}"))
            }
    }

    fun verifyRules(
        token: String,
        typeRules: List<ValidationType>,
        personId: UUID? = null
    ): Mono<ValidationCheckStatus.ValidationRuleResponse> {
        logger.info("Getting validations status rules")
        return identityClient.verifyRules(token, typeRules, personId)
    }
}