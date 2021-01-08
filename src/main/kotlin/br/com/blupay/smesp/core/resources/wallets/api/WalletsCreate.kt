package br.com.blupay.smesp.core.resources.wallets.api

import br.com.blupay.smesp.core.resources.wallets.models.PopulateResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import reactor.core.publisher.Mono

object WalletsCreate {
    @RequestMapping("wallets")
    interface Controller {

        @PostMapping("/populate")
        fun populate(auth: JwtAuthenticationToken): Mono<ResponseEntity<PopulateResponse>>
    }
}