package br.com.blupay.smesp.sellers

import br.com.blupay.blubasemodules.core.extensions.authCredentials
import br.com.blupay.smesp.core.resources.sellers.api.SellerBankAccount
import br.com.blupay.smesp.core.resources.sellers.api.SellerCreate
import br.com.blupay.smesp.core.resources.sellers.api.SellerRead
import br.com.blupay.smesp.core.resources.sellers.models.BankResponse
import br.com.blupay.smesp.core.resources.sellers.models.SellerResponse
import br.com.blupay.smesp.core.resources.shared.models.PasswordRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.util.UUID
import javax.annotation.security.RolesAllowed

@RestController
class SellerController(
        private val sellerService: SellerService,
) : SellerRead.Controller, SellerCreate.Controller, SellerBankAccount.Controller {

    @RolesAllowed("ROLE_GUEST", "ROLE_SELLER")
    override fun findOneByCnpj(cnpj: String, auth: JwtAuthenticationToken): ResponseEntity<SellerResponse> {
        val seller = sellerService.findOneByCnpj(cnpj, auth.token)
        return ResponseEntity.ok(seller)
    }

    override fun findOne(id: UUID, auth: JwtAuthenticationToken): ResponseEntity<SellerResponse> {
        val data = sellerService.findOne(id, auth.authCredentials)
        return ResponseEntity.ok(data)
    }

    @RolesAllowed("ROLE_SELLER")
    override fun createBankAccountsToSeller(
        sellerId: UUID,
        requestBody: SellerBankAccount.Request,
        auth: JwtAuthenticationToken
    ): ResponseEntity<BankResponse> {

        val bankAccount = sellerService.createBankAccount(sellerId, requestBody)
        return ResponseEntity.status(HttpStatus.CREATED).body(bankAccount)
    }

    @RolesAllowed("ROLE_SELLER")
    override fun findBankAccountsFromSeller(sellerId: UUID,
                                            auth: JwtAuthenticationToken): ResponseEntity<List<BankResponse>?> {
        val bankAccounts = sellerService.findBankAccounts(sellerId)
        return ResponseEntity.ok(bankAccounts)
    }

    @RolesAllowed("ROLE_GUEST", "ROLE_SELLER")
    override fun createCredentials(sellerId: UUID, request: PasswordRequest,
                                   auth: JwtAuthenticationToken): Mono<ResponseEntity<SellerResponse>> {
        return sellerService.createCredentials(sellerId, request, auth.authCredentials)
            .map { response -> ResponseEntity.status(HttpStatus.CREATED).body(response) }
    }
}
