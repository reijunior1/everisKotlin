package br.com.blupay.smesp.core.providers.token.wallet

import java.util.UUID

data class Wallet(
        val party: String,
        val id: UUID,
        val alias: String,
        val publicKey: String
)

data class WalletResponse(
        val wallet: Wallet,
        val roles: List<WalletRole>
)

data class BalanceResponse(
        val balance: Long
)