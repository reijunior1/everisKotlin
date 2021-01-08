package br.com.blupay.smesp.token

import br.com.blupay.smesp.core.providers.token.auth.AccessToken
import br.com.blupay.smesp.core.providers.token.auth.AuthProvider
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Service
class AuthService(
        private val authProvider: AuthProvider
) {
    private var token: AccessToken = AccessToken(null, 0)

    fun accessToken(): Mono<AccessToken> {
        return if (!token.isActive()) {
            authProvider.accessToken().map {
                token = it
                token
            }
        } else {
            return token.toMono()
        }
    }
}