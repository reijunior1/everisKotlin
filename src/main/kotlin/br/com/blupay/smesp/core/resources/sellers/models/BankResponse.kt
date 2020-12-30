package br.com.blupay.smesp.core.resources.sellers.models

import com.fasterxml.jackson.annotation.JsonInclude
import java.util.UUID

@JsonInclude(JsonInclude.Include.NON_NULL)
data class BankResponse(
    val id: UUID,
    val name: String,
    val cnpj: String,
    val agency: String,
    val account: String,
    val pix: String? = null
)
