package br.com.blupay.smesp.core.resources.wallets.exceptions

import br.com.blupay.blubasemodules.core.exceptions.BaseRuntimeException
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(FORBIDDEN)
class WalletOwnerException(owner: String) : BaseRuntimeException(
    message = "Wallet does not belong to user $owner",
    errors = Pair("Wrong user", owner),
    category = "WALLET_BELONGS_TO_ANOTHER_USER"
)