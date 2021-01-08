package br.com.blupay.smesp.core.providers.token.node

import br.com.blupay.blubasemodules.core.clients.ReactiveClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class NodeClient(
    @Value("\${provider.token.host}") private val baseUrl: String,
    @Value("\${provider.token.endpoint.node.public-key}") private val nodePublicKey: String,
    @Value("\${provider.token.endpoint.node.party}") private val nodeParty: String
) : ReactiveClient(baseUrl) {
    fun publicKey(token: String): Mono<NodeResponse>? {
        return get(nodePublicKey)
            .contentType(MediaType.APPLICATION_JSON)
            .authBearer(token)
            .send(NodeResponse::class.java)
    }

    fun party(token: String, pk: String): Mono<NodeResponse>? {
        return get(nodeParty)
            .contentType(MediaType.APPLICATION_JSON)
            .header("public-key", pk)
            .authBearer(token)
            .send(NodeResponse::class.java)
    }
}