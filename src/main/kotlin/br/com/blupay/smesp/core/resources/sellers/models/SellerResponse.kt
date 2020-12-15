package br.com.blupay.smesp.core.resources.sellers.models

import br.com.blupay.smesp.core.resources.enums.OnboardFlow
import br.com.blupay.smesp.core.resources.enums.OnboardFlow.CREDENTIALS
import java.util.UUID

data class SellerResponse(
        val id: UUID,
        val name: String,
        val cnpj: String,
        val email: String,
        val phone: String,
        val flow: OnboardFlow = CREDENTIALS
)