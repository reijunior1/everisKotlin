package br.com.blupay.smesp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SmespApplication

fun main(args: Array<String>) {
	runApplication<SmespApplication>(*args)
}
