package one.digitalInnovation.digibank.testes

import one.digitalInnovation.digibank.Funcionario

class ImprimeRelatorioFuncionario {
    companion object{
        fun imprime(funcionario: Funcionario){
            println(funcionario.toString())
        }
    }
}