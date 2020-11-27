package br.com.blupay.smesp.citizens

import java.util.UUID
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "citizens")
data class Citizen(
        @Id
        val id: UUID?,
        val cpf: String,
        val email: String,
        val phone: String,
        val onboarded: Boolean = false
)