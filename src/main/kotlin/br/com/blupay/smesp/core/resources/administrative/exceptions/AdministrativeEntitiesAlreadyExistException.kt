package br.com.blupay.smesp.core.resources.administrative.exceptions

import br.com.blupay.blubasemodules.core.exceptions.BaseRuntimeException
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(NOT_FOUND)
class AdministrativeEntitiesAlreadyExistException() : BaseRuntimeException(
        message = "Administrative entities already exist",
        category = "ENTITIES_ALREADY_EXIST"
)