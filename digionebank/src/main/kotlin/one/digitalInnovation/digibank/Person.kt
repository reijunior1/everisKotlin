package one.digitalInnovation.digibank

class Person {
    var name: String = "Reinaldo"
    var cpf: String = "111.222.333.44"
    private set
}
    fun main(){

        val reinaldo = Person()

        println(reinaldo)
        println(reinaldo.name)
        println(reinaldo.cpf)

    }