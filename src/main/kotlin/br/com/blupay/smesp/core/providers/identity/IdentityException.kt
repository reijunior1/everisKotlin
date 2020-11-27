package br.com.blupay.smesp.core.providers.identity

import br.com.blupay.blubasemodules.core.exceptions.BaseRuntimeException
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(NOT_FOUND)
class IdentityException(requestKey: String) : BaseRuntimeException(
        message = "Person not found in Identity",
        errors = linkedMapOf(Pair("key", requestKey)),
        category = "IDENTITY_PERSON_NOT_FOUND_EXCEPTION"
)