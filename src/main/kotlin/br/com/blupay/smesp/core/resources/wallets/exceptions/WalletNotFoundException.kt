package br.com.blupay.smesp.core.resources.wallets.exceptions

import br.com.blupay.blubasemodules.core.exceptions.BaseRuntimeException
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(NOT_FOUND)
class WalletNotFoundException(owner: String, token: String) : BaseRuntimeException(
        message = "Wallet with owner $owner and token $token not found",
        errors = Pair("identifier", owner),
        category = "WALLET_NOT_FOUND"
)