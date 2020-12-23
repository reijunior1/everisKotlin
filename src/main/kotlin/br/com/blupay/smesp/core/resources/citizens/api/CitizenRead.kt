package br.com.blupay.smesp.core.resources.citizens.api

import br.com.blupay.smesp.core.resources.citizens.models.CitizenResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import java.util.UUID

object CitizenRead {
    @RequestMapping("citizens")
    interface Controller {

        @GetMapping("{citizenId}")
        fun findOne(
                @PathVariable(value = "citizenId") citizenId: UUID,
                auth: JwtAuthenticationToken
        ): ResponseEntity<CitizenResponse>

        @GetMapping("find-by-cpf/{cpf}")
        fun findOneByCpf(
                @PathVariable(value = "cpf") cpf: String,
                auth: JwtAuthenticationToken
        ): ResponseEntity<CitizenResponse>
    }
}