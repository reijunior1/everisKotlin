package br.com.blupay.smesp.core.resources.banks.api

import br.com.blupay.smesp.core.resources.banks.models.BankResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import java.util.UUID

object BankRead {
    @RequestMapping("bank")
    interface Controller {

        @GetMapping("{sellerId}/bank-accounts")
        fun findAll(
            @PathVariable("sellerId") sellerId: UUID
        ): ResponseEntity<List<BankResponse>?>
    }
}