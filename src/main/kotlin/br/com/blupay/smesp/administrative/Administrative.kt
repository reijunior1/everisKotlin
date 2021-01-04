package br.com.blupay.smesp.administrative

import java.util.UUID
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "administrative")
data class Administrative(
        @Id
        val id: UUID,
        val name: String
)