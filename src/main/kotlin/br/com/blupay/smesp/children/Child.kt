package br.com.blupay.smesp.children

import br.com.blupay.smesp.children.categories.Category
import br.com.blupay.smesp.citizens.Citizen
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "children")
data class Child(
    @Id
    val id: UUID,
    val name: String,
    @ManyToOne
    val category: Category,
    @ManyToOne
    val citizen: Citizen
)

