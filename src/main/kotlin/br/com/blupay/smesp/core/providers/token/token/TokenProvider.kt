package br.com.blupay.smesp.core.providers.token.token

import br.com.blupay.smesp.core.providers.token.TokenException
import br.com.blupay.smesp.core.services.JwsService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class TokenProvider(
        private val tokenClient: TokenClient,
        private val jwsService: JwsService
) {
    private val logger: Logger = LoggerFactory.getLogger(TokenProvider::class.java)

    fun issueToken(token: String, data: IssueTokensRequest, privateKey: String, publicKey: String): Mono<IssueTokensRequest> {
        logger.info("Issue Token")
        val signData = jwsService.sign(data, privateKey, publicKey)
        return tokenClient.issueToken(token, signData) ?: throw TokenException("ISSUE_TOKEN_ERROR")
    }

    fun moveToken(token: String, data: MoveTokensRequest, privateKey: String, publicKey: String): Mono<MoveTokensRequest> {
        logger.info("Move Token")
        val signData = jwsService.sign(data, privateKey, publicKey)
        return tokenClient.moveToken(token, signData) ?: throw TokenException("MOVE_TOKEN_ERROR")
    }

    fun redeemToken(token: String, data: RedeemTokensRequest, privateKey: String, publicKey: String): Mono<RedeemTokensRequest> {
        logger.info("Redeem Token")
        val signData = jwsService.sign(data, privateKey, publicKey)
        return tokenClient.redeemToken(token, signData) ?: throw TokenException("REDEEM_TOKEN_ERROR")
    }

    fun safeMoveToken(token: String, data: SafeMoveTokensRequest, privateKey: String, publicKey: String): Mono<SafeMoveTokensRequest> {
        logger.info("Safe Move Token")
        val signData = jwsService.sign(data, privateKey, publicKey)
        return tokenClient.safeMoveToken(token, signData) ?: throw TokenException("SAFE_MOVE_TOKEN_ERROR")
    }

    fun payIoyToken(token: String, data: IoyMoveTokensRequest, privateKey: String, publicKey: String): Mono<IoyMoveTokensRequest> {
        logger.info("Pay IOY")
        val signData = jwsService.sign(data, privateKey, publicKey)
        return tokenClient.payIoyToken(token, signData) ?: throw TokenException("PAY_IOY_TOKEN_ERROR")
    }

    fun settlementOrderToken(token: String, data: SettlementMoveTokensRequest, privateKey: String, publicKey: String): Mono<SettlementMoveTokensRequest> {
        logger.info("Token Settlement Order")
        val signData = jwsService.sign(data, privateKey, publicKey)
        return tokenClient.settlementOrderToken(token, signData) ?: throw TokenException("TOKEN_SETTLEMENT_ORDER_ERROR")
    }
}