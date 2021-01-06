package br.com.blupay.smesp.transactions

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
        val debtorName: String? = null,
        @ManyToOne
        val debtorWallet: Wallet? = null,
        val creditorName: String? = null,
        @ManyToOne
        val creditorWallet: Wallet? = null,
        val amount: Long,
        @Enumerated(EnumType.STRING)
        val type: Type,
        val receipt: String? = null,
        @Enumerated(EnumType.STRING)
        val status: Status
) {
    enum class Type { CASHIN, SAFE_MOVE_CREDITOR }
    enum class Status { NEW }
}