package br.com.blupay.smesp.core.resources.citizens.models

import com.opencsv.bean.CsvBindByPosition

data class CitizenImport(

    @CsvBindByPosition(position = 0)
    val cpf: String? = null,

    @CsvBindByPosition(position = 1)
    val qtdAlunosBercario: String? = null,

    @CsvBindByPosition(position = 2)
    val qtdAlunosMiniGrupo: String? = null,

    @CsvBindByPosition(position = 3)
    val qtdAlunosInfantil: String? = null,

    @CsvBindByPosition(position = 4)
    val qtdAlunosAlfabetizacao: String? = null,

    @CsvBindByPosition(position = 5)
    val qtdAlunosInterdisciplinar: String? = null,

    @CsvBindByPosition(position = 6)
    val qtdAlunosAutoral: String? = null,

    @CsvBindByPosition(position = 7)
    val qtdAlunosMedioEjaMova: String? = null,

    @CsvBindByPosition(position = 8)
    val email: String? = null,

    @CsvBindByPosition(position = 9)
    val celular: String? = null
) {

    fun noNullCpf() = cpf ?: ""
    fun noNullEmail() = email ?: ""
    fun noNullPhone() = celular ?: ""

}
