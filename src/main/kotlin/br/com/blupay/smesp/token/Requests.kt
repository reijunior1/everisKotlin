package br.com.blupay.smesp.token

import java.util.UUID

data class WalletTemp(
        val id: UUID,
        val privateKey: String,
        val publicKey: String
)

data class Creditor(
        val id: UUID,
        val amount: Long
)