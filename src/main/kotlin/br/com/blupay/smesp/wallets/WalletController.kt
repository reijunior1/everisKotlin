package br.com.blupay.smesp.wallets

import br.com.blupay.smesp.core.resources.wallets.api.WalletRead
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class WalletController(private val walletService: WalletService) : WalletRead.Controller {
    override fun getWallet(id: UUID): ResponseEntity<Wallet?> {
        val wallet = walletService.findWalletById(id)
        return ResponseEntity.ok(wallet)
    }

}