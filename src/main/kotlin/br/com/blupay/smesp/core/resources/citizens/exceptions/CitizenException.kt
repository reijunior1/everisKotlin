package br.com.blupay.smesp.core.resources.citizens.exceptions

import br.com.blupay.blubasemodules.core.exceptions.BaseExceptionType
import br.com.blupay.blubasemodules.core.exceptions.BaseRuntimeException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class CitizenException(message: String? = null) : BaseRuntimeException(
        message = message ?: "Invalid request error",
        errors = listOf(),
        type = BaseExceptionType.INVALID_REQUEST_ERROR,
        category = "CITIZEN_INVALID_REQUEST_ERROR"
)
