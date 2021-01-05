package br.com.blupay.smesp.core.resources.administrative.api

import br.com.blupay.smesp.core.resources.administrative.models.AdminResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import reactor.core.publisher.Mono

object AdministrativeCreate {
    @RequestMapping("administrative")
    interface Controller {

        @PostMapping("admin-wallets")
        fun adminWallets(
                auth: JwtAuthenticationToken
        ): Mono<ResponseEntity<AdminResponse>>
    }
}