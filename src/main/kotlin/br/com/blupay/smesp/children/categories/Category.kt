package br.com.blupay.smesp.children.categories

import java.util.UUID
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "categories")
data class Category(
    @Id
    val id: UUID,
    val category: String,
    val price: Long
)