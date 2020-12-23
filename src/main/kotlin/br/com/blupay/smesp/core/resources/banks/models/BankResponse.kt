package br.com.blupay.smesp.core.resources.banks.models

import java.util.UUID

data class BankResponse(
    val id: UUID,
    val name: String,
    val cnpj: String,
    val agency: String,
    val account: String,
    val pix: String
)
