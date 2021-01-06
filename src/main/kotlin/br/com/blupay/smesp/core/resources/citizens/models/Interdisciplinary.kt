package br.com.blupay.smesp.core.resources.citizens.models

import br.com.blupay.smesp.children.Child
import br.com.blupay.smesp.children.categories.Category

class Interdisciplinary : StudentType {

    override fun resolve(citizenImport: CitizenImport, category: Category): List<Child> {
        if (citizenImport.qtdAlunosInterdisciplinar != null && citizenImport.qtdAlunosInterdisciplinar.toInt() > 0) {

            val elements = citizenImport.qtdAlunosInterdisciplinar.toInt()

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