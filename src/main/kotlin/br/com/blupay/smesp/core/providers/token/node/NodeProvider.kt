package br.com.blupay.smesp.core.providers.token.node

import br.com.blupay.smesp.core.providers.token.TokenException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class NodeProvider(
        private val nodeClient: NodeClient
) {
    private val logger: Logger = LoggerFactory.getLogger(NodeProvider::class.java)

    fun getPublicKey(bearerToken: String): Mono<NodeResponse> {
        logger.info("Get node public key")
        return nodeClient.publicKey(bearerToken)
            ?: throw TokenException("GET_PUBLIC_KEY_ERROR")
    }

    fun getParty(token: String, pk: String): Mono<NodeResponse> {
        logger.info("Get node party")
        return nodeClient.party(token, pk)
                ?: throw TokenException("GET_PARTY_ERROR")
    }
}