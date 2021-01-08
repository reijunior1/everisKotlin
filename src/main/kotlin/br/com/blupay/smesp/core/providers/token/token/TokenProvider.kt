package br.com.blupay.smesp.core.providers.token.token

import br.com.blupay.smesp.core.providers.token.TokenException
import br.com.blupay.smesp.core.providers.token.token.RedeemTokensRequest.Settlement
import br.com.blupay.smesp.core.providers.token.wallet.SettlementBalanceRequest
import br.com.blupay.smesp.core.services.JwsService
import br.com.blupay.smesp.token.AuthService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class TokenProvider(
    private val tokenClient: TokenClient,
    private val jwsService: JwsService,
    private val authService: AuthService
) {
    private val logger: Logger = LoggerFactory.getLogger(TokenProvider::class.java)

    fun issueToken(data: IssueTokensRequest, privateKey: String, publicKey: String): Mono<IssueTokensRequest> =
        authService.accessToken()
            .flatMap {
                logger.info("Issue Token")
                val signData = jwsService.sign(data, privateKey, publicKey)
                tokenClient.issueToken(it.access_token!!, signData) ?: throw TokenException("ISSUE_TOKEN_ERROR")
            }

    fun moveToken(data: MoveTokensRequest, privateKey: String, publicKey: String): Mono<MoveTokensRequest> =
        authService.accessToken()
            .flatMap {
                logger.info("Move Token")
                val signData = jwsService.sign(data, privateKey, publicKey)
                tokenClient.moveToken(it.access_token!!, signData) ?: throw TokenException("MOVE_TOKEN_ERROR")
            }

    fun redeemToken(data: RedeemTokensRequest, privateKey: String, publicKey: String): Mono<RedeemTokensRequest> =
        authService.accessToken()
            .flatMap {
                logger.info("Redeem Token")
                val signData = jwsService.sign(data, privateKey, publicKey)
                tokenClient.redeemToken(it.access_token!!, signData) ?: throw TokenException("REDEEM_TOKEN_ERROR")
            }

    fun safeMoveToken(data: SafeMoveTokensRequest, privateKey: String, publicKey: String): Mono<SafeMoveTokensRequest> =
        authService.accessToken()
            .flatMap {
                logger.info("Safe Move Token")
                val signData = jwsService.sign(data, privateKey, publicKey)
                tokenClient.safeMoveToken(it.access_token!!, signData) ?: throw TokenException("SAFE_MOVE_TOKEN_ERROR")
            }

    fun payIoyToken(data: IoyMoveTokensRequest, privateKey: String, publicKey: String): Mono<IoyMoveTokensRequest> =
        authService.accessToken()
            .flatMap {
                logger.info("Pay IOY")
                val signData = jwsService.sign(data, privateKey, publicKey)
                tokenClient.payIoyToken(it.access_token!!, signData) ?: throw TokenException("PAY_IOY_TOKEN_ERROR")
            }

    fun settlementOrderToken(
        data: SettlementMoveTokensRequest,
        privateKey: String,
        publicKey: String
    ): Mono<SettlementMoveTokensRequest> = authService.accessToken()
        .flatMap {
            logger.info("Token Settlement Order")
            val signData = jwsService.sign(data, privateKey, publicKey)
            tokenClient.settlementOrderToken(it.access_token!!, signData)
                ?: throw TokenException("TOKEN_SETTLEMENT_ORDER_ERROR")
        }

    fun settlementList(data: SettlementBalanceRequest, privateKey: String, publicKey: String): Mono<List<Settlement>> =
        authService.accessToken()
            .flatMap {
                logger.info("Token Settlement List")
                val signData = jwsService.sign(data, privateKey, publicKey)
                tokenClient.settlementList(it.access_token!!, signData)
                    ?: throw TokenException("TOKEN_SETTLEMENT_LIST_ERROR")
            }
}