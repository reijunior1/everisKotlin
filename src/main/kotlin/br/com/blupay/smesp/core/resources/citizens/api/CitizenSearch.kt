package br.com.blupay.smesp.core.resources.citizens.api

import br.com.blupay.smesp.core.resources.citizens.models.CitizenResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import javax.validation.constraints.NotBlank

object CitizenSearch {

    @RequestMapping("citizens")
    interface Controller {

        @GetMapping("search")
        fun search(
                query: Query,
                auth: JwtAuthenticationToken
        ): ResponseEntity<CitizenResponse>
    }

    data class Query(
            @NotBlank
            @RequestParam("cpf") val cpf: String,
    )
}