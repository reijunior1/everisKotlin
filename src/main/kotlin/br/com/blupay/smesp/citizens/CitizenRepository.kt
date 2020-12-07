package br.com.blupay.smesp.citizens

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CitizenRepository : JpaRepository<Citizen, UUID> {
    fun findByCpf(cpf: String): Citizen?
}