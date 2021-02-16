package one.digitalInnovation.digibank

class Pessoa {
    var name: String = "Reinaldo"
    var cpf: String = "111.222.333.44"
    private set

    constructor()

    fun personInfo() = "$name e $cpf"
}
    fun main(){

        val reinaldo = Pessoa()

//        println(reinaldo)
//        println(reinaldo.name)
        println(reinaldo.personInfo())

    }