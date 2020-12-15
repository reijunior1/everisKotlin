package br.com.blupay.smesp.core.resources.sellers.exceptions

import br.com.blupay.blubasemodules.core.exceptions.BaseRuntimeException
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(NOT_FOUND)
class SellerNotFoundException(identifier: String) : BaseRuntimeException(
        message = "Seller with identifier $identifier not found",
        errors = Pair("identifier", identifier),
        category = "SELLER_NOT_FOUND"
)