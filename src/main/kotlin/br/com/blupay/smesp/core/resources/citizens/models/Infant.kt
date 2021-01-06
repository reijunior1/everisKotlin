package br.com.blupay.smesp.core.resources.citizens.models

import br.com.blupay.smesp.children.Child
import br.com.blupay.smesp.children.categories.Category

class Infant : StudentType {
    override fun resolve(citizenImport: CitizenImport, category: Category): List<Child> {
        if (citizenImport.qtdAlunosInfantil != null && citizenImport.qtdAlunosInfantil.toInt() > 0) {

            val elements = citizenImport.qtdAlunosInfantil.toInt()

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