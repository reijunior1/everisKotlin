package br.com.blupay.smesp.core.providers.token.node

import br.com.blupay.smesp.core.providers.token.TokenException
import br.com.blupay.smesp.token.AuthService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class NodeProvider(
    private val nodeClient: NodeClient,
    private val authService: AuthService
) {
    private val logger: Logger = LoggerFactory.getLogger(NodeProvider::class.java)

    fun getPublicKey(): Mono<NodeResponse> = authService.accessToken()
        .flatMap {
            logger.info("Get node public key")
            nodeClient.publicKey(it.access_token!!)
                ?: throw TokenException("GET_PUBLIC_KEY_ERROR")
        }

    fun getParty(pk: String): Mono<NodeResponse> = authService.accessToken()
        .flatMap {
            logger.info("Get node party")
            nodeClient.party(it.access_token!!, pk)
                ?: throw TokenException("GET_PARTY_ERROR")
        }
}