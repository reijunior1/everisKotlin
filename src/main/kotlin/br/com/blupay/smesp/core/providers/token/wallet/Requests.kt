package br.com.blupay.smesp.core.providers.token.wallet

import br.com.blupay.smesp.core.providers.token.LocalWalletRequest
import br.com.blupay.smesp.core.providers.token.TokenHeader
import br.com.blupay.smesp.core.providers.token.TokenRequest

enum class WalletRole { PAYER, RECEIVER, ADMIN, IOY, CASHIN, CASHOUT }

data class GetWalletRequest(
        val header: TokenHeader,
        val wallet: LocalWalletRequest
)

data class BalanceRequest(
        val header: TokenHeader,
        val wallet: LocalWalletRequest,
        val token: TokenRequest
)

data class IoyBalanceRequest(
        val header: TokenHeader,
        val wallet: LocalWalletRequest,
        val token: TokenRequest,
        val type: IoyType
) {
    enum class IoyType { PAYER, RECEIVER }
}

data class SettlementBalanceRequest(
        val header: TokenHeader,
        val wallet: LocalWalletRequest,
        val token: TokenRequest,
        val type: SettlementType
) {
    enum class SettlementType { RECEIVER, CASHOUT }
}

// TODO: renomear para IssueWalletRequest
data class IssueWallet(
        val alias: String,
        val publicKey: String,
        val role: WalletRole
)

data class AddAndRemoveRoleRequest(
        val header: TokenHeader,
        val wallet: LocalWalletRequest,
        val role: WalletRole
)