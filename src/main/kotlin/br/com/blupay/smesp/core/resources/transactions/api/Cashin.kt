package br.com.blupay.smesp.core.resources.transactions.api

import br.com.blupay.smesp.core.resources.transactions.models.CashinRequest
import br.com.blupay.smesp.core.resources.transactions.models.CashinResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import reactor.core.publisher.Mono

object Cashin {
    @RequestMapping("transactions")
    interface Controller {

        @PostMapping("cashin")
        fun cashin(
                @RequestBody requestBody: CashinRequest,
                auth: JwtAuthenticationToken
        ): Mono<ResponseEntity<CashinResponse>>

    }
}