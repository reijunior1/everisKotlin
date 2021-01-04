package br.com.blupay.smesp.core.resources.shared.exceptions

import br.com.blupay.blubasemodules.core.exceptions.BaseRuntimeException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class UserNotFoundException(identifier: String) : BaseRuntimeException(
        message = "User with identifier $identifier not found",
        errors = Pair("identifier", identifier),
        category = "USER_NOT_FOUND"
)