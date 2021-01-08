package br.com.blupay.smesp

import java.io.File
import java.util.UUID

class RandomCpf {
    private fun randomize(n: Int): Int {
        return (Math.random() * n).toInt()
    }

    private fun mod(dividend: Int, divider: Int): Int {
        return Math.round(dividend - Math.floor(dividend / divider.toDouble()) * divider).toInt()
    }

    fun cpf(dots: Boolean): String {
        val n = 9
        val n1 = randomize(n)
        val n2 = randomize(n)
        val n3 = randomize(n)
        val n4 = randomize(n)
        val n5 = randomize(n)
        val n6 = randomize(n)
        val n7 = randomize(n)
        val n8 = randomize(n)
        val n9 = randomize(n)
        var d1 = n9 * 2 + n8 * 3 + n7 * 4 + n6 * 5 + n5 * 6 + n4 * 7 + n3 * 8 + n2 * 9 + n1 * 10
        d1 = 11 - mod(d1, 11)
        if (d1 >= 10) d1 = 0
        var d2 = d1 * 2 + n9 * 3 + n8 * 4 + n7 * 5 + n6 * 6 + n5 * 7 + n4 * 8 + n3 * 9 + n2 * 10 + n1 * 11
        d2 = 11 - mod(d2, 11)
        if (d2 >= 10) d2 = 0
        return if (dots) "$n1$n2$n3.$n4$n5$n6.$n7$n8$n9-$d1$d2" else "$n1$n2$n3$n4$n5$n6$n7$n8$n9$d1$d2"
    }
}

fun children(): Int{
    val rand = (0..5).random()
    return if (rand == 1) {
        rand
    } else {
        0
    }
}

fun main(args: Array<String>) {
    val gerador = RandomCpf()

    val content = mutableListOf<String>()
    for (i in 1..800000) {
        val cpf = gerador.cpf(false)
        val uuid = UUID.randomUUID().toString().replace("-", "").substring(0,16)
        val phone = (100000000..900000000).random()


        val line = "${cpf},${children()},${children()},${children()},${children()},${children()},${children()},${children()},${uuid}@gmail.com,${phone}"
        content.add(line)
    }

    File("/tmp/citizens-800.csv").writeText(content.joinToString("\n"))

    println("END FILE")
}
