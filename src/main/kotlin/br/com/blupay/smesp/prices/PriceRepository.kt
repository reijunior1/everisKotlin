package br.com.blupay.smesp.prices

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PriceRepository : JpaRepository<Price, Int> {
}