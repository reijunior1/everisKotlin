package br.com.blupay.smesp.core.providers.token.token

import br.com.blupay.blubasemodules.core.clients.ReactiveClient
import br.com.blupay.smesp.core.providers.token.token.RedeemTokensRequest.Settlement
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class TokenClient(
        @Value("\${provider.token.host}") private val baseUrl: String,
        @Value("\${provider.token.endpoint.token.issue}") private val issueToken: String,
        @Value("\${provider.token.endpoint.token.move}") private val moveToken: String,
        @Value("\${provider.token.endpoint.token.redeem}") private val redeemToken: String,
        @Value("\${provider.token.endpoint.token.safe-move}") private val safeMoveToken: String,
        @Value("\${provider.token.endpoint.token.pay-ioy}") private val payIoyToken: String,
        @Value("\${provider.token.endpoint.token.settlement-order}") private val settlementOrderToken: String,
        @Value("\${provider.token.endpoint.token.settlement-list}") private val settlementListToken: String
) : ReactiveClient(baseUrl) {

    fun issueToken(bearerToken: String, data: String): Mono<IssueTokensRequest>? {
        return doPostRequest(issueToken, bearerToken, data)
    }

    fun moveToken(bearerToken: String, data: String): Mono<MoveTokensRequest>? {
        return doPutRequest(moveToken, bearerToken, data)
    }

    fun redeemToken(bearerToken: String, data: String): Mono<RedeemTokensRequest>? {
        return doPutRequest(redeemToken, bearerToken, data)
    }

    fun safeMoveToken(bearerToken: String, data: String): Mono<SafeMoveTokensRequest>? {
        return doPutRequest(safeMoveToken, bearerToken, data)
    }

    fun payIoyToken(bearerToken: String, data: String): Mono<IoyMoveTokensRequest>? {
        return doPutRequest(payIoyToken, bearerToken, data)
    }

    fun settlementOrderToken(bearerToken: String, data: String): Mono<SettlementMoveTokensRequest>? {
        return doPutRequest(settlementOrderToken, bearerToken, data)
    }

    fun settlementList(bearerToken: String, data: String): Mono<List<Settlement>>? {
        return doGetRequest(settlementListToken,  bearerToken, data)
    }

    private inline fun <reified T> doGetRequest(url: String, bearerToken: String, data: String): Mono<T>? {
        return get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .authBearer(bearerToken)
                .header("signature", data)
                .send(T::class.java)
    }

    private inline fun <reified T> doPostRequest(url: String, bearerToken: String, data: String): Mono<T>? {
        return post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .authBearer(bearerToken)
                .body(
                        BodyRequest(data)
                )
                .send(T::class.java)
    }

    private inline fun <reified T> doPutRequest(url: String, bearerToken: String, data: String): Mono<T>? {
        return put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .authBearer(bearerToken)
                .body(
                        BodyRequest(data)
                )
                .send(T::class.java)
    }

    data class BodyRequest(
            val data: String
    )
}