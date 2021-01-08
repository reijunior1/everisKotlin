package br.com.blupay.smesp.wallets

import br.com.blupay.blubasemodules.core.extensions.authCredentials
import br.com.blupay.smesp.core.providers.token.wallet.BalanceResponse
import br.com.blupay.smesp.core.resources.wallets.api.WalletRead
import br.com.blupay.smesp.core.resources.wallets.api.WalletRead.ApprovedBalanceResponse
import br.com.blupay.smesp.core.resources.wallets.api.WalletsCreate
import br.com.blupay.smesp.core.resources.wallets.models.PopulateResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.util.UUID
import javax.annotation.security.RolesAllowed

@RestController
class WalletController(
    private val walletService: WalletService
) : WalletRead.Controller, WalletsCreate.Controller {
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

    override fun approvedBalances(
        id: UUID,
        auth: JwtAuthenticationToken
    ): ResponseEntity<List<ApprovedBalanceResponse>> {
        val approved =
            walletService.getApprovedBalanceAndReleaseDate(id, auth.authCredentials)

        return ResponseEntity.ok().body(approved)
    }

    @RolesAllowed("ROLE_ADMIN")
    override fun populate(auth: JwtAuthenticationToken): Mono<ResponseEntity<PopulateResponse>> {
        return walletService
            .createWallets(auth.token.tokenValue)
            .map {
                val res = ResponseEntity.status(HttpStatus.CREATED).body(it)
                res
            }
    }

}