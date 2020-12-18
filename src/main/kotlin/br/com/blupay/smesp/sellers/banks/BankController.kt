package br.com.blupay.smesp.sellers.banks

import br.com.blupay.smesp.core.resources.banks.api.BankRead
import br.com.blupay.smesp.core.resources.banks.models.BankResponse
import br.com.blupay.smesp.sellers.Seller
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class BankController(private val bankService: BankService): BankRead.Controller {
    override fun findAll(sellerId: UUID): ResponseEntity<List<BankResponse>?> {
        val data = bankService.findAll(sellerId)
        return ResponseEntity.ok(data!!)
    }
}