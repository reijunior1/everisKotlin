package br.com.blupay.smesp.core.providers.token.wallet

import br.com.blupay.smesp.core.providers.token.TokenException
import br.com.blupay.smesp.core.services.JwsService
import br.com.blupay.smesp.token.AuthService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class WalletProvider(
    private val walletClient: WalletClient,
    private val jwsService: JwsService,
    private val authService: AuthService
) {
    private val logger: Logger = LoggerFactory.getLogger(WalletProvider::class.java)

    fun getWallet(data: GetWalletRequest, privateKey: String, publicKey: String): Mono<WalletResponse> =
        authService.accessToken()
            .flatMap {
                logger.info("Get Wallet")
                val signData = jwsService.sign(data, privateKey, publicKey)
                walletClient.getWallet(it.access_token!!, signData) ?: throw TokenException("GET_WALLET_ERROR")
            }

    fun balance(data: BalanceRequest, privateKey: String, publicKey: String): Mono<BalanceResponse> =
        authService.accessToken()
            .flatMap {
                logger.info("Get Wallet Balance")
                val signData = jwsService.sign(data, privateKey, publicKey)
                walletClient.balance(it.access_token!!, signData) ?: throw TokenException("GET_WALLET_BALANCE_ERROR")
            }

    fun ioyBalance(data: IoyBalanceRequest, privateKey: String, publicKey: String): Mono<BalanceResponse> =
        authService.accessToken()
            .flatMap {
                logger.info("Get IOY Wallet Balance")
                val signData = jwsService.sign(data, privateKey, publicKey)
                walletClient.ioyBalance(it.access_token!!, signData)
                    ?: throw TokenException("GET_WALLET_IOY_BALANCE_ERROR")
            }

    fun settlementBalance(
        data: SettlementBalanceRequest,
        privateKey: String,
        publicKey: String
    ): Mono<BalanceResponse> = authService.accessToken()
        .flatMap {
            logger.info("Get Settlement Wallet Balance")
            val signData = jwsService.sign(data, privateKey, publicKey)
            walletClient.settlementBalance(it.access_token!!, signData)
                ?: throw TokenException("GET_WALLET_SETTLEMENT_BALANCE_ERROR")
        }

    fun issueWallet(wallet: IssueWallet): Mono<WalletTokenResponse> = authService.accessToken()
        .flatMap {
            logger.info("Create Wallet")
            walletClient.issueWallet(it.access_token!!, wallet) ?: throw TokenException("ISSUE_WALLET_ERROR")
        }

    fun addRole(data: AddAndRemoveRoleRequest, privateKey: String, publicKey: String): Mono<AddAndRemoveRoleRequest> =
        authService.accessToken()
            .flatMap {
                logger.info("Add Wallet Role")
                val signData = jwsService.sign(data, privateKey, publicKey)
                walletClient.addRole(it.access_token!!, signData) ?: throw TokenException("ADD_WALLET_ROLE_ERROR")
            }
}