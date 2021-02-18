package one.digitalInnovation.digibank.testes

import one.digitalInnovation.digibank.Banco

fun main(){

    val digiOneBank = Banco(nome ="DigiOne",numero = 12)

    println(digiOneBank.nome)
    println(digiOneBank.numero)

    val digiOneBank2 = digiOneBank.copy(nome ="Reinaldo Couto",13)


    println(digiOneBank2.info())
}