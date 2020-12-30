package br.com.blupay.smesp.wallets

import br.com.blupay.smesp.core.resources.shared.enums.UserTypes
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
        val type: UserTypes,
        val publicKey: String,
        val privateKey: String
)