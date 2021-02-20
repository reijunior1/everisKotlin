package one.digitalInnovation.digibank.testes

import one.digitalInnovation.digibank.Cliente
import one.digitalInnovation.digibank.ClienteTipo

fun main() {
    val reinaldo = Cliente(
        nome = "Reinaldo Junior",
        cpf = "789456123",
        clienteTipo = ClienteTipo.PF,
        senha = "12"
    )

    println(reinaldo)

    TesteAUtenticacao().autentica(reinaldo)

}


