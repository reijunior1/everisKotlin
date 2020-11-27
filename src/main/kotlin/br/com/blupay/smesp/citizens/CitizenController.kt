package br.com.blupay.smesp.citizens

import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("citizens")
class CitizenController(val citizenService: CitizenService) {
    @GetMapping("find")
    fun findByCpf(request: CitizenSearch.Request, auth: JwtAuthenticationToken):
            ResponseEntity<CitizenSearch.Response> {
        val data = citizenService.findByCpf(request.cpf, auth.token.tokenValue)
        return ResponseEntity.ok(data)
    }

}