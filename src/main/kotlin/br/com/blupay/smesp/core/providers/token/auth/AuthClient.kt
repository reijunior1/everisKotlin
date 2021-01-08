package br.com.blupay.smesp.core.providers.token.auth

import br.com.blupay.blubasemodules.core.clients.ReactiveClient
import br.com.blupay.blubasemodules.core.models.FormData
import br.com.blupay.blubasemodules.core.models.OAuth2TokenResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class AuthClient(
    @Value("\${provider.token.auth.url}") private val baseUrl: String,
    @Value("\${provider.token.auth.uri}") private val authUri: String,
    @Value("\${provider.token.auth.grant_type}") private val grantType: String,
    @Value("\${provider.token.auth.client_id}") private val clientId: String,
    @Value("\${provider.token.auth.client_secret}") private val clientSecret: String,
    @Value("\${provider.token.auth.username}") private val username: String,
    @Value("\${provider.token.auth.password}") private val password: String
) : ReactiveClient(baseUrl) {

    fun accessToken(): Mono<AccessToken> {
        return post(authUri)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .header(HttpHeaders.AUTHORIZATION, "Basic YmFja2VuZC1zZXJ2aWNlOnIxcmpQUXpOeW1qblBQdEsyNUxRN1h2VG1aNmRSRWVh")
            .body(
                FormData()
                    .add("client_id", clientId)
                    .add("client_secret", clientSecret)
                    .add("grant_type", grantType)
                    .add("username", username)
                    .add("password", password)
            )
            .send(AccessToken::class.java)!!
    }
}