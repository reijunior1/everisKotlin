package br.com.blupay.smesp.core.providers.token.auth

import br.com.blupay.smesp.core.providers.token.TokenException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class AuthProvider(
        private val client: AuthClient
) {
    private val logger: Logger = LoggerFactory.getLogger(AuthProvider::class.java)

    fun accessToken(): Mono<AccessToken> {
        logger.info("Get bearer token")
        return client.accessToken()
    }
}