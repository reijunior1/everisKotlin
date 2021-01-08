package br.com.blupay.smesp.core.resources.wallets.models

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class PopulateResponse(
    val count: Long
)