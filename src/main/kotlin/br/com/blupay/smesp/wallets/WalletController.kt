package br.com.blupay.smesp.wallets

import br.com.blupay.blubasemodules.core.extensions.authCredentials
import br.com.blupay.smesp.core.providers.token.wallet.BalanceResponse
import br.com.blupay.smesp.core.resources.wallets.api.WalletRead
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class WalletController(private val walletService: WalletService) : WalletRead.Controller {
    override fun getWallet(id: UUID): ResponseEntity<Wallet?> {
        val wallet = walletService.findById(id)
        return ResponseEntity.ok(wallet)
    }

    override fun getBalance(id: UUID, auth: JwtAuthenticationToken): ResponseEntity<BalanceResponse> {
        val balance = walletService.getBalance(id, auth.authCredentials)
        return ResponseEntity.ok(balance)
    }

    override fun getSettlementBalance(
        id: UUID,
        auth: JwtAuthenticationToken
    ): ResponseEntity<BalanceResponse> {
        val balance = walletService.getSettlementBalance(id, auth.authCredentials)
        return ResponseEntity.ok(balance)
    }
}