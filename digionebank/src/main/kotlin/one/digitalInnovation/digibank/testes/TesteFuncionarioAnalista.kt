package one.digitalInnovation.digibank.testes

import one.digitalInnovation.digibank.Analista

fun main(){
    val reinaldo = Analista("Reinaldo", "11122233344",1000.0)
        ImprimeRelatorioFuncionario.imprime(reinaldo)
    }