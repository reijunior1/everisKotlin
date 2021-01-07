package br.com.blupay.smesp.core.providers.token.wallet

import br.com.blupay.smesp.core.providers.token.TokenException
import br.com.blupay.smesp.core.services.JwsService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class WalletProvider(
        private val walletClient: WalletClient,
        private val jwsService: JwsService
) {
    private val logger: Logger = LoggerFactory.getLogger(WalletProvider::class.java)

    fun getWallet(token: String, data: GetWalletRequest, privateKey: String, publicKey: String): Mono<WalletResponse> {
        logger.info("Get Wallet")
        val signData = jwsService.sign(data, privateKey, publicKey)
        return walletClient.getWallet(token, signData) ?: throw TokenException("GET_WALLET_ERROR")
    }

    fun balance(token: String, data: BalanceRequest, privateKey: String, publicKey: String): Mono<BalanceResponse> {
        logger.info("Get Wallet Balance")
        val signData = jwsService.sign(data, privateKey, publicKey)
        return walletClient.balance(token, signData) ?: throw TokenException("GET_WALLET_BALANCE_ERROR")
    }

    fun ioyBalance(token: String, data: IoyBalanceRequest, privateKey: String, publicKey: String): Mono<BalanceResponse> {
        logger.info("Get IOY Wallet Balance")
        val signData = jwsService.sign(data, privateKey, publicKey)
        return walletClient.ioyBalance(token, signData) ?: throw TokenException("GET_WALLET_IOY_BALANCE_ERROR")
    }

    fun settlementBalance(token: String, data: SettlementBalanceRequest, privateKey: String, publicKey: String): Mono<BalanceResponse> {
        logger.info("Get Settlement Wallet Balance")
        val signData = jwsService.sign(data, privateKey, publicKey)
        return walletClient.settlementBalance(token, signData) ?: throw TokenException("GET_WALLET_SETTLEMENT_BALANCE_ERROR")
    }

    fun issueWallet(token: String, wallet: IssueWallet): Mono<WalletTokenResponse> {
        logger.info("Create Wallet")
        return walletClient.issueWallet(token, wallet) ?: throw TokenException("ISSUE_WALLET_ERROR")
    }

    fun addRole(token: String, data: AddAndRemoveRoleRequest, privateKey: String, publicKey: String): Mono<AddAndRemoveRoleRequest> {
        logger.info("Add Wallet Role")
        val signData = jwsService.sign(data, privateKey, publicKey)
        return walletClient.addRole(token, signData) ?: throw TokenException("ADD_WALLET_ROLE_ERROR")
    }
}