package br.com.blupay.smesp.sellers

import br.com.blupay.smesp.core.resources.shared.enums.OnboardFlow
import br.com.blupay.smesp.core.resources.shared.enums.OnboardFlow.CREDENTIALS
import br.com.blupay.smesp.sellers.banks.BankAccount
import java.util.UUID
import javax.persistence.CascadeType.ALL
import javax.persistence.Entity
import javax.persistence.EnumType.STRING
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "sellers")
data class Seller(
    @Id
    val id: UUID?,
    val name: String,
    val cnpj: String,
    val email: String,
    val phone: String,
    @OneToMany(cascade = [ALL], mappedBy = "seller")
    val banks: List<BankAccount>?,
    @Enumerated(STRING)
    val flow: OnboardFlow = CREDENTIALS
)