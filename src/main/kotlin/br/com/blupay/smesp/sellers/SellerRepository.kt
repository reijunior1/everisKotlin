package br.com.blupay.smesp.sellers

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface SellerRepository : JpaRepository<Seller, UUID> {
    fun findByCnpj(cnpj: String): Seller?
}