package com.acme.tour.controller

import com.acme.tour.model.Funcionario
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class ControllerFuncionarios {

    @GetMapping("/funcionario")
    fun getFuncionario(): Funcionario {
        return Funcionario(UUID.randomUUID(), "", "", "21-03-1981", true, 1, 4000.00)
    }

    fun created(): Funcionario{
        return Funcionario()
    }
}
