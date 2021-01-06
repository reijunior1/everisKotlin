package br.com.blupay.smesp.core.resources.citizens.api

import br.com.blupay.smesp.core.resources.citizens.models.CitizenStatusResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import reactor.core.publisher.Mono
import java.util.UUID

object CitizenStatus {

    @RequestMapping("citizens")
    interface Controller {

        @GetMapping("{citizenId}/status")
        fun checkStatus(
            @PathVariable(value = "citizenId") citizenId: UUID,
            auth: JwtAuthenticationToken
        ): Mono<ResponseEntity<CitizenStatusResponse>>
    }
}