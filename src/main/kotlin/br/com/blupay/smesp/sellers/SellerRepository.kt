package br.com.blupay.smesp.sellers

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface SellerRepository : JpaRepository<Seller, UUID> {
    fun findByCnpj(cnpj: String): Seller?
}