package br.com.blupay.smesp.core.providers.token.wallet

import br.com.blupay.smesp.wallets.Wallet
import java.util.UUID

data class WalletTokenResponse(
        val party: String,
        val id: UUID,
        val alias: String,
        val publicKey: String
)

data class WalletResponse(
        val wallet: WalletTokenResponse,
        val roles: List<Wallet.Role>
)

data class BalanceResponse(
        val balance: Long
)