package br.com.blupay.smesp.core.resources.sellers.api

import br.com.blupay.smesp.core.resources.shared.models.PasswordRequest
import br.com.blupay.smesp.core.resources.sellers.models.SellerResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import java.util.UUID
import javax.validation.Valid

object SellerCreate {
    @RequestMapping("sellers")
    interface Controller {
        @PostMapping("{sellerId}/credentials")
        fun createCredentials(@PathVariable(value = "sellerId") sellerId: UUID,
                              @Valid @RequestBody request: PasswordRequest,
                              auth: JwtAuthenticationToken): ResponseEntity<SellerResponse>
    }
}