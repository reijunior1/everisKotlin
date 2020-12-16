package br.com.blupay.smesp.sellers.banks

import java.util.UUID
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name= "bankAccounts")
data class BankAccount(
    @Id
    val id: UUID,
    val name: String,
    val cnpj: String,
    val agency: String,
    val account: String
)
