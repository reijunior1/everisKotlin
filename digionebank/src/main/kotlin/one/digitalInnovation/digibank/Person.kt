package one.digitalInnovation.digibank

class Person {
    var name: String = "Reinaldo"
    var cpf: String = "111.222.555-44"
}

fun main(){
    val reinaldo = Person()

    println(reinaldo.name)
    print(reinaldo.cpf)
}