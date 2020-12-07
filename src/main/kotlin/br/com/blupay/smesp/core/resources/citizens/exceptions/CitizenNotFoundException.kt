package br.com.blupay.smesp.core.resources.citizens.exceptions

import br.com.blupay.blubasemodules.core.exceptions.BaseRuntimeException
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(NOT_FOUND)
class CitizenNotFoundException(identifier: String) : BaseRuntimeException(
        message = "Citizen with identifier $identifier not found",
        errors = Pair("identifier", identifier),
        category = "CITIZEN_NOT_FOUND"
)