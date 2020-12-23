package br.com.blupay.smesp.sellers.banks

import br.com.blupay.smesp.core.resources.banks.models.BankResponse
import br.com.blupay.smesp.sellers.SellerService
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class BankService(
    private val sellerService: SellerService
) {
    fun findAll(sellerId: UUID): List<BankResponse>? {
        val banks = sellerService.findById(sellerId).banks
        return banks?.map { it ->
            BankResponse(
                it.id,
                it.name,
                it.cnpj,
                it.agency,
                it.account,
                it.pix
            )
        }
    }
}