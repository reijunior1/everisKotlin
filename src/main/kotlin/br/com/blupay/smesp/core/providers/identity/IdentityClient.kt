package br.com.blupay.smesp.core.providers.identity

import br.com.blupay.blubasemodules.core.clients.ReactiveClient
import br.com.blupay.blubasemodules.identity.people.PersonCreate
import br.com.blupay.blubasemodules.identity.people.PersonCredentials
import br.com.blupay.blubasemodules.identity.people.PersonSearch
import br.com.blupay.blubasemodules.identity.people.PersonSearch.PersonList
import br.com.blupay.blubasemodules.identity.validations.ValidationCheckStatus
import br.com.blupay.blubasemodules.shared.validations.enums.ValidationType
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.UUID

@Component
class IdentityClient(
    @Value("\${provider.identity.host}") private val baseUrl: String,
    @Value("\${provider.identity.endpoint.people.root}") private val endpointPeople: String,
    @Value("\${provider.identity.endpoint.people.credentials}") private val endpointPeopleCredentials: String,
    @Value("\${provider.identity.endpoint.validations.verify-rules}") private val endpointVerifyRules: String
) : ReactiveClient(baseUrl) {

    fun createPerson(bearerToken: String, body: PersonCreate.Request): Mono<PersonCreate.Response> {
        return post(endpointPeople)
            .contentType(MediaType.APPLICATION_JSON)
            .authBearer(bearerToken)
            .body(body)
            .send(PersonCreate.Response::class.java)!!
    }

    fun createPersonCredentials(bearerToken: String, personId: UUID, body: PersonCredentials.Request): Mono<Boolean> {
        return post(endpointPeopleCredentials, personId)
            .contentType(MediaType.APPLICATION_JSON)
            .authBearer(bearerToken)
            .body(body)
            .send()!!
            .map { response -> HttpStatus.CREATED == response.statusCode() }
    }

    fun peopleSearch(bearerToken: String, query: PersonSearch.Query): Mono<PersonList>? {
        return get(endpointPeople)
            .contentType(MediaType.APPLICATION_JSON)
            .authBearer(bearerToken)
            .queryParam(query.params())
            .send(PersonList::class.java)
    }

    fun verifyRules(
        bearerToken: String,
        typeRules: List<ValidationType>,
        personId: UUID?
    ): Mono<ValidationCheckStatus.ValidationRuleResponse> {
        return get(endpointVerifyRules)
            .contentType(MediaType.APPLICATION_JSON)
            .authBearer(bearerToken)
            .uri { uriBuilder ->
                typeRules.forEach { uriBuilder.queryParam("type", it) }
                if (personId != null) {
                    uriBuilder.queryParam("personId", personId)
                }
            }
            .send(ValidationCheckStatus.ValidationRuleResponse::class.java)!!
    }
}
