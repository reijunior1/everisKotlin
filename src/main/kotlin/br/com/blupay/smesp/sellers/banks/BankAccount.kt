package br.com.blupay.smesp.sellers.banks

import br.com.blupay.smesp.sellers.Seller
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name= "bankAccounts")
data class BankAccount(
    @Id
    val id: UUID,
    val name: String,
    val cnpj: String,
    val agency: String,
    val account: String,
    @ManyToOne
    val seller: Seller?
)
