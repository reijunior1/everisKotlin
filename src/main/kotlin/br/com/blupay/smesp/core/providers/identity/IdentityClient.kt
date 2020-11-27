package br.com.blupay.smesp.core.providers.identity

import br.com.blupay.blubasemodules.core.clients.ReactiveClient
import br.com.blupay.blubasemodules.identity.people.PersonSearch.PersonList
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class IdentityClient(
        @Value("\${provider.host.identity}") private val baseUrl: String,
        @Value("\${provider.endpoint.get-person}") private val getPersonEndpoint: String
) : ReactiveClient(baseUrl) {
    fun getByCpf(cpf: String, bearerToken: String): Mono<PersonList>? {
        return get(getPersonEndpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .authBearer(bearerToken)
                .queryParam("cpf", cpf)
                .send(PersonList::class.java)
    }

}