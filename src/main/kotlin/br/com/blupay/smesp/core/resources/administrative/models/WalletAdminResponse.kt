package br.com.blupay.smesp.core.resources.administrative.models

import java.util.UUID

data class WalletAdminResponse(
        val administrativeId: UUID,
        val walletId: UUID,
        val tokenWalletId: UUID,
        val alias: String,
        val issuer: String,
        val publicKey: String
)