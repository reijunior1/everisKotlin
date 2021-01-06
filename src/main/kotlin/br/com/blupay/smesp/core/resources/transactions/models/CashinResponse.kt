package br.com.blupay.smesp.core.resources.transactions.models

import br.com.blupay.smesp.transactions.Transaction

data class CashinResponse(
        val transaction: Transaction
)