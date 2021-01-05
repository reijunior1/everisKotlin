package br.com.blupay.smesp.transactions

import br.com.blupay.smesp.sellers.Seller
import br.com.blupay.smesp.wallets.Wallet
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "transactions")
data class Transaction(
        @Id
        val id: UUID,
        val hash: String,
        val date: LocalDateTime,
        @ManyToOne
        val debtor: Wallet,
        @ManyToOne
        val creditor: Wallet,
        val amount: Long,
        @Enumerated(EnumType.STRING)
        val type: TransactionType
) {
    enum class TransactionType { SAFE_MOVE_CREDITOR }
}