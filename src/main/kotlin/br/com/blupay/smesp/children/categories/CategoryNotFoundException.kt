package br.com.blupay.smesp.children.categories

import br.com.blupay.blubasemodules.core.exceptions.BaseExceptionType
import br.com.blupay.blubasemodules.core.exceptions.BaseRuntimeException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class CategoryNotFoundException(category: String? = "category") : BaseRuntimeException(
    message = "This $category not found",
    errors = listOf(),
    type = BaseExceptionType.INVALID_REQUEST_ERROR,
    category = "CATEGORY_NOT_FOUND_ERROR"
)
