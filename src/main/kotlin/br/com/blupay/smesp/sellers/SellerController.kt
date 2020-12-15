package br.com.blupay.smesp.sellers

import br.com.blupay.smesp.core.resources.sellers.api.SellerRead
import br.com.blupay.smesp.core.resources.sellers.models.SellerResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.RestController

@RestController
class SellerController(private val sellerService: SellerService) : SellerRead.Controller {
    override fun findOneByCnpj(cnpj: String, auth: JwtAuthenticationToken): ResponseEntity<SellerResponse> {
        val seller = sellerService.findOneByCnpj(cnpj, auth.token)
        return ResponseEntity.ok(seller)
    }
}
