package br.com.blupay.smesp.core.providers.token.token

import br.com.blupay.smesp.core.providers.token.LocalWalletRequest
import br.com.blupay.smesp.core.providers.token.TokenHeader
import br.com.blupay.smesp.core.providers.token.TokenRequest
import br.com.blupay.smesp.core.providers.token.WalletRequest
import java.util.UUID

data class CreditorRequest(
        val wallet: WalletRequest,
        val amount: Long
)

data class IssueTokensRequest(
        val header: TokenHeader,
        val wallet: WalletRequest,
        val token: TokenRequest
)

data class MoveTokensRequest(
        val header: TokenHeader,
        val debtor: LocalWalletRequest,
        val creditors: List<CreditorRequest>,
        val token: TokenRequest
)

data class SafeMoveTokensRequest(
        val header: TokenHeader,
        val dueDate: Long,
        val debtor: LocalWalletRequest,
        val creditors: List<CreditorRequest>,
        val ioyWallet: WalletRequest,
        val token: TokenRequest
)

data class IoyMoveTokensRequest(
        val header: TokenHeader,
        val ioyId: UUID,
        var ioy: Ioy? = null
) {
    data class Ioy(
            val id: UUID,
            val date: Long,
            val dueDate: Long,
            val from: Wallet,
            val to: Wallet,
            val token: Token,
            val amount: Long
    )
    data class Wallet(
            val id: UUID,
            val party: String
    )
    data class Token(
            val issuer: String,
            val type: String
    )
}

data class SettlementMoveTokensRequest(
        val header: TokenHeader,
        val dueDate: Long,
        val wallet: LocalWalletRequest,
        val cashoutWallet: WalletRequest,
        val token: TokenRequest,
        var amount: Long? = null
)

data class RedeemTokensRequest(
        val header: TokenHeader,
        val settlementId: UUID,
        var settlement: Settlement? = null
) {
    class Settlement(
            val id: UUID,
            val date: Long,
            val dueDate: Long,
            val wallet: Wallet,
            val cashoutWallet: Wallet,
            val token: Token,
            val amount: Long
    )
    data class Wallet(
            val id: UUID,
            val party: String
    )
    data class Token(
            val issuer: String,
            val type: String
    )
}