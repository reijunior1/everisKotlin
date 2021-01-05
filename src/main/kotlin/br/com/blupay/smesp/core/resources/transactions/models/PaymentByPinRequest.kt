package br.com.blupay.smesp.core.resources.transactions.models

import java.util.UUID

data class PaymentByPinRequest(
        val debtor: UUID,
        val pin: String,
        val amount: Long
)