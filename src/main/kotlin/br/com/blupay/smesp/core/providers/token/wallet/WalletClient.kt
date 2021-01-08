package br.com.blupay.smesp.core.providers.token.wallet

import br.com.blupay.blubasemodules.core.clients.ReactiveClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class WalletClient(
    @Value("\${provider.token.host}") private val baseUrl: String,
    @Value("\${provider.token.endpoint.wallet.get}") private val getWallet: String,
    @Value("\${provider.token.endpoint.wallet.balance}") private val balance: String,
    @Value("\${provider.token.endpoint.wallet.ioy-balance}") private val ioyBalance: String,
    @Value("\${provider.token.endpoint.wallet.settlement-balance}") private val settlementBalance: String,
    @Value("\${provider.token.endpoint.wallet.issue}") private val issueWallet: String,
    @Value("\${provider.token.endpoint.wallet.add-role}") private val addWalletRole: String
) : ReactiveClient(baseUrl) {

    fun issueWallet(token: String, wallet: IssueWallet): Mono<WalletTokenResponse>? {
        return post(issueWallet)
            .contentType(MediaType.APPLICATION_JSON)
            .authBearer(token)
            .body(wallet)
            .send(WalletTokenResponse::class.java)

    }

    fun getWallet(token: String, data: String): Mono<WalletResponse>? {
        return doGetRequest(getWallet, token, data)
    }

    fun balance(token: String, data: String): Mono<BalanceResponse>? {
        return doGetRequest(balance, token, data)
    }

    fun ioyBalance(token: String, data: String): Mono<BalanceResponse>? {
        return doGetRequest(ioyBalance, token, data)
    }

    fun settlementBalance(token: String, data: String): Mono<BalanceResponse>? {
        return doGetRequest(settlementBalance, token, data)
    }

    fun addRole(token: String, data: String): Mono<AddAndRemoveRoleRequest>? {
        return doPostRequest(addWalletRole, token, data)
    }

    private inline fun <reified T> doGetRequest(url: String, token: String, data: String): Mono<T>? {
        return get(url)
            .contentType(MediaType.APPLICATION_JSON)
            .authBearer(token)
            .header("signature", data)
            .send(T::class.java)
    }

    private inline fun <reified T> doPostRequest(url: String, token: String, data: String): Mono<T>? {
        return post(url)
            .contentType(MediaType.APPLICATION_JSON)
            .authBearer(token)
            .body(
                BodyRequest(data)
            )
            .send(T::class.java)
    }

    data class BodyRequest(
        val data: String
    )

}