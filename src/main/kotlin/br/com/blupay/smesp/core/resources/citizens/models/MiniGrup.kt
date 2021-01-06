package br.com.blupay.smesp.core.resources.citizens.models

import br.com.blupay.smesp.children.Child
import br.com.blupay.smesp.children.categories.Category
import br.com.blupay.smesp.citizens.Citizen
import br.com.blupay.smesp.citizens.CitizenRepository
import java.util.UUID

class MiniGrup : StudentType {

    override fun resolve(citizenImport: CitizenImport, category: Category): List<Child> {
        if (citizenImport.qtdAlunosMiniGrupo != null && citizenImport.qtdAlunosMiniGrupo.toInt() > 0) {

            val elements = citizenImport.qtdAlunosMiniGrupo.toInt()

            return List(elements) {
                Child(
                    name = "Filho",
                    category = category
                )
            }
        }
        return emptyList()
    }
}