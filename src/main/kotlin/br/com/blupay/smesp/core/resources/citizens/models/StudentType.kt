package br.com.blupay.smesp.core.resources.citizens.models

import br.com.blupay.smesp.children.Child
import br.com.blupay.smesp.children.categories.Category

interface StudentType {

    fun resolve(citizenImport: CitizenImport, category: Category): List<Child>
}