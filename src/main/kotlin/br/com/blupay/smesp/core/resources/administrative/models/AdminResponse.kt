package br.com.blupay.smesp.core.resources.administrative.models

import java.util.UUID

data class AdminResponse(
        val id: UUID,
        val document: String,
        val name: String,
        val wallets: List<WalletAdmin>
) {
    data class WalletAdmin(
            val walletId: UUID,
            val tokenWalletId: UUID,
            val alias: String,
            val issuer: String,
            val publicKey: String
    )
}