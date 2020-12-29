package br.com.blupay.smesp.core.resources.wallets.api

import br.com.blupay.smesp.wallets.Wallet
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import java.util.UUID

object WalletRead {
    @RequestMapping("wallets")
    interface Controller {
        @GetMapping("{id}")
        fun getWallet(@PathVariable(value = "id") id: UUID):
                ResponseEntity<Wallet?>
    }
}