package br.com.blupay.smesp.citizens

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CitizenRepository : JpaRepository<Citizen, UUID> {
    fun findByCpf(cpf: String): Citizen?

    @Query("select c from Citizen c where c.id not in (select owner from Wallet)")
    fun findCitizensWithoutWallet(pageable: Pageable): Page<Citizen>
}