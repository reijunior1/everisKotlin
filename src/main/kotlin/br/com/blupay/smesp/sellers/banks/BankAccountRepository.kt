package br.com.blupay.smesp.sellers.banks

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface BankAccountRepository: JpaRepository<BankAccount, UUID> {
}