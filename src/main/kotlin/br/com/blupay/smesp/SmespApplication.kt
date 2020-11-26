package br.com.blupay.smesp

import br.com.blupay.blubasemodules.BaseApp
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["br.com.blupay"])
class SmespApplication : BaseApp()

fun main(args: Array<String>) {
    runApplication<SmespApplication>(*args)
}
