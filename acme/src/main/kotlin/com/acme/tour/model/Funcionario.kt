package com.acme.tour.model

import com.fasterxml.jackson.annotation.JsonTypeInfo
import org.intellij.lang.annotations.Identifier
import java.util.*

data class Funcionario(

    val id: UUID,
    val nome: String,
    val cpf: String,
    val dataNascimento: String,
    val poisFilhos: Boolean,
    val qtdFilhos: Int,
    val salario: Double

)