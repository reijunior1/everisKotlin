package br.com.blupay.smesp.wallets

import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EnumType.STRING
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "wallets")
data class Wallet(
        @Id
        val id: UUID?,
        val owner: UUID,
        val token: UUID,
        @Enumerated(STRING)
        val type: TypeWallet
)

enum class TypeWallet {
    CITIZEN, SELLER
}