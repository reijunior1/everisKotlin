package br.com.blupay.smesp.core.resources.citizens.models

import br.com.blupay.blubasemodules.core.models.ResponseStatus
import br.com.blupay.blubasemodules.core.models.ResponseStatus.PENDING
import br.com.blupay.blubasemodules.shared.validations.models.ValidationStatusResponse
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CitizenStatusResponse(val status: ResponseStatus? = PENDING,
                                 val validations: List<ValidationStatusResponse?>? = null)