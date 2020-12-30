package br.com.blupay.smesp.sellers.banks

import br.com.blupay.smesp.sellers.Seller
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "bankAccounts")
data class BankAccount(
        @Id
        val id: UUID,
        val name: String,
        val cnpj: String,
        val agency: String,
        val account: String,
        val pix: String? = null,
        @ManyToOne
        val seller: Seller? = null
) {
    constructor(name: String,
                cnpj: String,
                agency: String,
                account: String,
                seller: Seller? = null,
                pix: String? = null) : this(
            id = UUID.randomUUID(),
            name = name,
            cnpj = cnpj,
            agency = agency,
            account = account,
            seller = seller,
            pix = pix,
    )
}
