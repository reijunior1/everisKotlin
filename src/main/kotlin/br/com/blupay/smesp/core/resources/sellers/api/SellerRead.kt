package br.com.blupay.smesp.core.resources.sellers.api

import br.com.blupay.smesp.core.resources.sellers.models.SellerResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

object SellerRead {
    @RequestMapping("sellers")
    interface Controller {

        @GetMapping("find-by/{cnpj}")
        fun findOneByCnpj(
                @PathVariable("cnpj") cnpj: String,
                auth: JwtAuthenticationToken
        ): ResponseEntity<SellerResponse>
    }

}