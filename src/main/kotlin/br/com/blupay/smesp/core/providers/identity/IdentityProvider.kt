package br.com.blupay.smesp.core.providers.identity

import br.com.blupay.blubasemodules.identity.people.PersonSearch.Response
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class IdentityProvider(
        private val identityClient: IdentityClient
) {
    private val logger: Logger = LoggerFactory.getLogger(IdentityProvider::class.java)

    fun findPersonByCpf(token: String, cpf: String): Response {
        logger.info("Getting data from Identity by CPF of citizen")

        val personList = identityClient.getByCpf(token, cpf)?.block() ?: throw IdentityException(cpf)
        return personList.data[0]
//        return CitizenSearch.Response(person.id, person.cpf, person.email, person.phone)
    }
}