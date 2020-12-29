package br.com.blupay.smesp.token

import br.com.blupay.smesp.core.providers.token.node.NodeProvider
import br.com.blupay.smesp.core.providers.token.node.NodeResponse
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class TokenNodeService(
        private val nodeProvider: NodeProvider
) {
    var nodePk: String? = null

    fun getPublicKey(token: String): Mono<String> {
        return if (nodePk != null) {
            Mono.just(nodePk!!)
        } else {
            nodeProvider.getPublicKey(token).map {
                nodePk = it.publicKey
                it.publicKey
            }
        }
    }

    fun getParty(token: String, pk: String): Mono<NodeResponse> {
        return nodeProvider.getParty(token, pk)
    }
}