package br.com.blupay.smesp.core.resources.citizens.models

import br.com.blupay.smesp.core.resources.enums.OnboardFlow
import br.com.blupay.smesp.core.resources.enums.OnboardFlow.CREDENTIALS
import java.util.UUID

data class CitizenResponse(
        val id: UUID,
        val name: String,
        val cpf: String,
        val email: String,
        val phone: String,
        val flow: OnboardFlow = CREDENTIALS
)