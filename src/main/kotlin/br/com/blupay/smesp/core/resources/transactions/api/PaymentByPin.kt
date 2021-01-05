package br.com.blupay.smesp.core.resources.transactions.api

import br.com.blupay.smesp.core.resources.transactions.models.PaymentByPinRequest
import br.com.blupay.smesp.core.resources.transactions.models.PaymentResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import reactor.core.publisher.Mono

object PaymentByPin {
    @RequestMapping("transactions")
    interface Controller {

        @PostMapping("payment-by-pin")
        fun paymentByPin(
                @RequestBody requestBody: PaymentByPinRequest,
                auth: JwtAuthenticationToken
        ): Mono<ResponseEntity<PaymentResponse>>

    }
}