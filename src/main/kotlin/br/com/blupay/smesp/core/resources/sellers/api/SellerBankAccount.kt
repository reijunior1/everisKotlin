package br.com.blupay.smesp.core.resources.sellers.api

import br.com.blupay.smesp.core.resources.sellers.models.BankResponse
import br.com.blupay.smesp.core.resources.sellers.models.SellerResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import java.util.UUID

object SellerBankAccount {

    @RequestMapping("sellers")
    interface Controller {

        @PostMapping("{sellerId}/bank-accounts")
        fun createBankAccountsToSeller(
                @PathVariable("sellerId") sellerId: UUID,
                @RequestBody requestBody: Request,
                auth: JwtAuthenticationToken
        ): ResponseEntity<BankResponse>

        @GetMapping("{sellerId}/bank-accounts")
        fun findBankAccountsFromSeller(
                @PathVariable("sellerId") sellerId: UUID,
                auth: JwtAuthenticationToken
        ): ResponseEntity<List<BankResponse>?>
    }

    data class Request(val name: String,
                       val agency: String,
                       val account: String,
                       val pix: String? = null)
}