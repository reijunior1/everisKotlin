package br.com.blupay.smesp.core.resources.citizens.api

import br.com.blupay.smesp.core.resources.citizens.models.CitizenResponse
import br.com.blupay.smesp.core.resources.shared.models.PasswordRequest
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import reactor.core.publisher.Mono
import java.util.UUID
import javax.validation.Valid

object CitizenCreate {
    @RequestMapping("citizens")
    interface Controller {

        @PostMapping("{citizenId}/credentials")
        fun createCredentials(
                @PathVariable(value = "citizenId") citizenId: UUID,
                @Valid @RequestBody request: PasswordRequest,
                auth: JwtAuthenticationToken
        ): Mono<ResponseEntity<CitizenResponse>>
    }
}