package br.com.blupay.smesp.core.resources.wallets.exceptions

import br.com.blupay.blubasemodules.core.exceptions.BaseRuntimeException
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(NOT_FOUND)
class WalletNotFoundException(identifier: String) : BaseRuntimeException(
        message = "Wallet with identifier $identifier not found",
        errors = Pair("identifier", identifier),
        category = "WALLET_NOT_FOUND"
)