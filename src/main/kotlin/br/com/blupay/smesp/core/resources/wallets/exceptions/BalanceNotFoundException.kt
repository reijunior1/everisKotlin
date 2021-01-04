package br.com.blupay.smesp.core.resources.wallets.exceptions

import br.com.blupay.blubasemodules.core.exceptions.BaseRuntimeException
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(NOT_FOUND)
class BalanceNotFoundException(identifier: String) : BaseRuntimeException(
    message = "Balance not found for $identifier",
    errors = Pair("identifier", identifier),
    category = "BALANCE_NOT_FOUND"
)