package br.com.blupay.smesp.core.resources.administrative.exceptions

import br.com.blupay.blubasemodules.core.exceptions.BaseRuntimeException
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(NOT_FOUND)
class AdministrativeNotFoundException(identifier: String) : BaseRuntimeException(
        message = "Administrative with identifier $identifier not found",
        errors = Pair("identifier", identifier),
        category = "ADMINISTRATIVE_NOT_FOUND"
)