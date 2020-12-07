package br.com.blupay.smesp.core.providers.identity

import br.com.blupay.blubasemodules.core.clients.ReactiveClient
import br.com.blupay.blubasemodules.identity.people.PersonCredentials
import br.com.blupay.blubasemodules.identity.people.PersonSearch
import br.com.blupay.blubasemodules.identity.people.PersonSearch.PersonList
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.UUID

@Component
class IdentityClient(
        @Value("\${provider.host.identity}") private val baseUrl: String,
        @Value("\${provider.endpoint.people}") private val endpointPeople: String,
        @Value("\${provider.endpoint.people-credentials}") private val endpointPeopleCredentials: String
) : ReactiveClient(baseUrl) {
    fun peopleSearch(bearerToken: String, query: PersonSearch.Query): Mono<PersonList>? {
        return get(endpointPeople)
                .contentType(MediaType.APPLICATION_JSON)
                .authBearer(bearerToken)
                .queryParam(query.params())
                .send(PersonList::class.java)
    }

    fun createPersonCredentials(bearerToken: String, personId: UUID, body: PersonCredentials.Request): Mono<Boolean> {
        return post(endpointPeopleCredentials, personId)
                .contentType(MediaType.APPLICATION_JSON)
                .authBearer(bearerToken)
                .body(body)
                .send()!!
                .map { response -> HttpStatus.CREATED == response.statusCode() }
    }

}