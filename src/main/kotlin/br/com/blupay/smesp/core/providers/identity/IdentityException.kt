package br.com.blupay.smesp.core.providers.identity

import br.com.blupay.blubasemodules.core.exceptions.BaseExceptionType.INVALID_REQUEST_ERROR
import br.com.blupay.blubasemodules.core.exceptions.BaseRuntimeException
import org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(UNPROCESSABLE_ENTITY)
class IdentityException(message: String? = null) : BaseRuntimeException(
        message = message ?: "Invalid request error",
        errors = linkedMapOf(),
        type = INVALID_REQUEST_ERROR,
        category = "PROVIDER_REQUEST_ERROR"
)