package br.com.blupay.smesp.citizens

import br.com.blupay.smesp.core.resources.enums.OnboardFlow
import br.com.blupay.smesp.core.resources.enums.OnboardFlow.CREDENTIALS
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EnumType.STRING
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "citizens")
data class Citizen(
        @Id
        val id: UUID?,
        val name: String,
        val cpf: String,
        val email: String,
        val phone: String,
        @Enumerated(STRING)
        val flow: OnboardFlow = CREDENTIALS
)