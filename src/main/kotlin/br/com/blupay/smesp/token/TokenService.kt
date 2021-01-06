package br.com.blupay.smesp.token

import br.com.blupay.smesp.core.providers.token.LocalWalletRequest
import br.com.blupay.smesp.core.providers.token.TokenHeader
import br.com.blupay.smesp.core.providers.token.TokenRequest
import br.com.blupay.smesp.core.providers.token.WalletRequest
import br.com.blupay.smesp.core.providers.token.node.NodeProvider
import br.com.blupay.smesp.core.providers.token.token.CreditorRequest
import br.com.blupay.smesp.core.providers.token.token.IoyMoveTokensRequest
import br.com.blupay.smesp.core.providers.token.token.IssueTokensRequest
import br.com.blupay.smesp.core.providers.token.token.MoveTokensRequest
import br.com.blupay.smesp.core.providers.token.token.RedeemTokensRequest
import br.com.blupay.smesp.core.providers.token.token.SafeMoveTokensRequest
import br.com.blupay.smesp.core.providers.token.token.SettlementMoveTokensRequest
import br.com.blupay.smesp.core.providers.token.token.TokenProvider
import br.com.blupay.smesp.wallets.Wallet
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.UUID

@Service
class TokenService(
        @Value("\${service.token.type}") private val tokenType: String,
        private val nodeProvider: NodeProvider,
        private val tokenProvider: TokenProvider
) {
    fun issueToken(token: String, signer: Wallet, id: UUID, amount: Long, maxSize: Long): Mono<IssueTokensRequest> {
        return nodeProvider.getPublicKey(token).map { node ->
            IssueTokensRequest(
                    TokenHeader(
                            action = "br.com.blupay.token.workflows.token.IssueTokens",
                            wallet = signer.token
                    ),
                    WalletRequest(
                            id,
                            node.publicKey ?: ""
                    ),
                    TokenRequest(
                            quantity = amount,
                            type = tokenType,
                            maxSize = maxSize
                    )
            )
        }.flatMap { data ->
            tokenProvider.issueToken(token, data, signer.privateKey, signer.publicKey)
        }
    }

    fun moveToken(
            token: String,
            signer: WalletData,
            debtorId: UUID,
            creditors: List<Creditor>
    ): Mono<MoveTokensRequest> {
        return nodeProvider.getPublicKey(token).map { node ->
            MoveTokensRequest(
                    TokenHeader(
                            action = "br.com.blupay.token.workflows.token.MoveTokens",
                            wallet = signer.id
                    ),
                    LocalWalletRequest(debtorId),
                    creditors.map {
                        CreditorRequest(
                                WalletRequest(
                                        it.id,
                                        node.publicKey ?: ""
                                ),
                                it.amount
                        )
                    },
                    TokenRequest(
                            issuer = node.publicKey,
                            type = tokenType
                    )
            )
        }.flatMap { data ->
            tokenProvider.moveToken(token, data, signer.privateKey, signer.publicKey)
        }
    }

    fun redeemToken(token: String, signer: WalletData, settlementId: UUID): Mono<RedeemTokensRequest> {
        val data = RedeemTokensRequest(
                TokenHeader(
                        action = "br.com.blupay.token.workflows.token.RedeemTokens",
                        wallet = signer.id
                ),
                settlementId
        )
        return tokenProvider.redeemToken(token, data, signer.privateKey, signer.publicKey)
    }

    fun safeMoveToken(
            token: String,
            signer: Wallet,
            dueDate: Long,
            debtorId: UUID,
            ioyWalletId: UUID,
            creditors: List<Creditor>
    ): Mono<SafeMoveTokensRequest> {
        return nodeProvider.getPublicKey(token).map { node ->
            SafeMoveTokensRequest(
                    TokenHeader(
                            action = "br.com.blupay.token.workflows.token.SafeMoveTokens",
                            wallet = signer.token
                    ),
                    dueDate,
                    LocalWalletRequest(debtorId),
                    creditors.map {
                        CreditorRequest(
                                WalletRequest(
                                        it.id,
                                        node.publicKey ?: ""
                                ),
                                it.amount
                        )
                    },
                    WalletRequest(
                            ioyWalletId,
                            node.publicKey ?: ""
                    ),
                    TokenRequest(
                            issuer = node.publicKey,
                            type = tokenType
                    )
            )
        }.flatMap { data ->
            tokenProvider.safeMoveToken(token, data, signer.privateKey, signer.publicKey)
        }
    }

    fun payIoyToken(token: String, signer: WalletData, ioyId: UUID): Mono<IoyMoveTokensRequest> {
        val data = IoyMoveTokensRequest(
                TokenHeader(
                        action = "br.com.blupay.token.workflows.token.PayIoy",
                        wallet = signer.id
                ),
                ioyId
        )
        return tokenProvider.payIoyToken(token, data, signer.privateKey, signer.publicKey)
    }

    fun settlementOrderToken(
            token: String,
            signer: WalletData,
            dueDate: Long,
            debtorId: UUID,
            cashoutWalletId: UUID,
            creditors: List<Creditor>
    ): Mono<SettlementMoveTokensRequest> {
        return nodeProvider.getPublicKey(token).map { node ->
            SettlementMoveTokensRequest(
                    TokenHeader(
                            action = "br.com.blupay.token.workflows.token.SettlementOrder",
                            wallet = signer.id
                    ),
                    dueDate,
                    LocalWalletRequest(debtorId),
                    WalletRequest(
                            cashoutWalletId,
                            node.publicKey ?: ""
                    ),
                    TokenRequest(
                            issuer = node.publicKey,
                            type = tokenType
                    )
            )
        }.flatMap { data ->
            tokenProvider.settlementOrderToken(token, data, signer.privateKey, signer.publicKey)
        }
    }
}