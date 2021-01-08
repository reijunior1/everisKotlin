package br.com.blupay.smesp.citizens

import br.com.blupay.smesp.children.Child
import br.com.blupay.smesp.core.resources.shared.enums.OnboardFlow
import br.com.blupay.smesp.core.resources.shared.enums.OnboardFlow.CREDENTIALS
import java.util.UUID
import javax.persistence.CascadeType.ALL
import javax.persistence.Entity
import javax.persistence.EnumType.STRING
import javax.persistence.Enumerated
import javax.persistence.FetchType.LAZY
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "citizens")
data class Citizen(
        @Id
        val id: UUID? = UUID.randomUUID(),
        val name: String,
        val cpf: String,
        val email: String? = null,
        val phone: String? = null,
        @Enumerated(STRING)
        val flow: OnboardFlow = CREDENTIALS
) {
    @OneToMany(cascade = [ALL], orphanRemoval = true, mappedBy = "citizen")
    var children: List<Child> = listOf()
        set(value) {
                field = value.map {
                        it.citizen = this
                        it
                }
        }
}