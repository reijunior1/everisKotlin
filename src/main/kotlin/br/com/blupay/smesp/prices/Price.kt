package br.com.blupay.smesp.prices

import java.time.Instant
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "prices")
data class Price(
    @Id
    val id: Int,
    val price: Long,
    val initialDate: Instant,
    val finalDate: Instant
)