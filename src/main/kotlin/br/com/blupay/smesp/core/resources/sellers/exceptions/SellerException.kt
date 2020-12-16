package br.com.blupay.smesp.core.resources.sellers.exceptions

import br.com.blupay.blubasemodules.core.exceptions.BaseExceptionType
import br.com.blupay.blubasemodules.core.exceptions.BaseRuntimeException
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(BAD_REQUEST)
class SellerException(message: String? = null) : BaseRuntimeException(
        message = message ?: "Invalid request error",
        errors = listOf(),
        type = BaseExceptionType.INVALID_REQUEST_ERROR,
        category = "SELLER_INVALID_REQUEST_ERROR"
)