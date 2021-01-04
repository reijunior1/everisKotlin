package br.com.blupay.smesp.core.resources.sellers.models

import br.com.blupay.smesp.core.resources.shared.enums.OnboardFlow
import br.com.blupay.smesp.core.resources.shared.enums.OnboardFlow.CREDENTIALS
import com.fasterxml.jackson.annotation.JsonInclude
import java.util.UUID

@JsonInclude(JsonInclude.Include.NON_NULL)
data class SellerResponse(
        val id: UUID,
        val name: String,
        val cnpj: String,
        val email: String,
        val phone: String,
        val flow: OnboardFlow = CREDENTIALS,
        val walletId: UUID? = null
)