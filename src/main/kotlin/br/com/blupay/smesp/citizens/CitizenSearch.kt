package br.com.blupay.smesp.citizens

import org.springframework.web.bind.annotation.RequestParam
import java.util.UUID

object CitizenSearch {
    data class Response(
            val id: UUID,
            val cpf: String,
            val email: String,
            val phone: String,
            val onboarded: Boolean = false
    )

    data class Request(@RequestParam("cpf") val cpf: String)
}