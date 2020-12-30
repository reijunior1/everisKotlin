package br.com.blupay.smesp.core.resources.shared.models

import br.com.blupay.blubasemodules.core.validators.annotations.ValidPassword
import com.fasterxml.jackson.annotation.JsonProperty

class PasswordRequest(
        @JsonProperty("password")
        @ValidPassword(minLength = 6, maxLength = 6)
        val password: String
)