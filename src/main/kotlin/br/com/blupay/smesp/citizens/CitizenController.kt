package br.com.blupay.smesp.citizens

import br.com.blupay.smesp.core.resources.citizens.api.CitizenCreate
import br.com.blupay.smesp.core.resources.citizens.api.CitizenRead
import br.com.blupay.smesp.core.resources.citizens.models.CitizenResponse
import br.com.blupay.smesp.core.resources.citizens.models.PasswordRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import javax.annotation.security.RolesAllowed

@RestController
class CitizenController(
        val citizenService: CitizenService
) : CitizenCreate.Controller, CitizenRead.Controller {

    @RolesAllowed("ROLE_GUEST", "ROLE_CITIZEN")
    override fun findOne(citizenId: UUID, auth: JwtAuthenticationToken): ResponseEntity<CitizenResponse> {
        val data = citizenService.findOne(citizenId, auth.token)
        return ResponseEntity.ok(data)
    }

    @RolesAllowed("ROLE_GUEST", "ROLE_CITIZEN")
    override fun findOneByCpf(cpf: String, auth: JwtAuthenticationToken): ResponseEntity<CitizenResponse> {
        val citizen = citizenService.findOneByCpf(cpf, auth.token)
        return ResponseEntity.ok(citizen)
    }

    @RolesAllowed("ROLE_GUEST", "ROLE_CITIZEN")
    override fun createCredentials(citizenId: UUID, request: PasswordRequest, auth: JwtAuthenticationToken): ResponseEntity<CitizenResponse> {
        val citizen = citizenService.createCredentials(citizenId, request, auth.token)
        return ResponseEntity.status(HttpStatus.CREATED).body(citizen)
    }
}