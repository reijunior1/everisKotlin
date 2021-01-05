package br.com.blupay.smesp.administrative

import br.com.blupay.smesp.core.providers.token.wallet.IssueWallet
import br.com.blupay.smesp.core.resources.administrative.exceptions.AdministrativeEntitiesAlreadyExistException
import br.com.blupay.smesp.core.resources.administrative.models.AdminResponse
import br.com.blupay.smesp.core.resources.shared.enums.UserTypes
import br.com.blupay.smesp.core.services.JwsService
import br.com.blupay.smesp.token.TokenWalletService
import br.com.blupay.smesp.wallets.Wallet
import br.com.blupay.smesp.wallets.WalletService
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import reactor.kotlin.core.util.function.component1
import reactor.kotlin.core.util.function.component2
import reactor.kotlin.core.util.function.component3
import java.util.UUID

@Service
class AdministrativeService(
        private val repository: AdministrativeRepository,
        private val jwsService: JwsService,
        private val walletService: WalletService,
        private val tokenWalletService: TokenWalletService
) {
    fun create(token: String, document: String): Mono<AdminResponse> = createEntity(document, "Blupay")
            .flatMap { administrative ->
                createWallet(token, administrative.id, "admin", Wallet.Role.ADMIN)
                        .flatMap { adminWallet ->

                            val cashinWallet = createWallet(token, administrative.id, "cashin", Wallet.Role.CASHIN) // não está transferindo para PAYER - mudar
                            val cashoutWallet = createWallet(token, administrative.id, "cashout", Wallet.Role.CASHOUT) // precisa ser receiver - mmudar
                            val ioyWallet = createWallet(token, administrative.id, "ioy", Wallet.Role.IOY) // precisa ser payer PAYER - mudar

                            Mono.zip(cashinWallet, cashoutWallet, ioyWallet)
                                    .map { (cashinWallet, cashoutWallet, ioyWallet) -> arrayOf(adminWallet, cashinWallet, cashoutWallet, ioyWallet) }
                                    .map {
                                        AdminResponse(
                                                id = administrative.id,
                                                document = administrative.document,
                                                name = administrative.name,
                                                wallets = it.toList()
                                        )
                                    }
                        }
            }

    private fun createEntity(document: String, name: String): Mono<Administrative> {
        if (repository.count() > 0) throw AdministrativeEntitiesAlreadyExistException()

        val administrative = Administrative(
                UUID.randomUUID(),
                document = document,
                name = name
        )
        return repository.save(administrative).toMono()
    }

    private fun createWallet(token: String, administrativeId: UUID, alias: String, role: Wallet.Role): Mono<AdminResponse.WalletAdmin> {
        val keyPair = jwsService.getKeyPairEncoded()
        val issueWallet = IssueWallet(
                alias,
                keyPair.publicKey,
                role
        )
        return tokenWalletService.issueWallet(token, issueWallet).map { tokenWallet ->
            val wallet = walletService.save(
                    administrativeId,
                    tokenWallet.id,
                    UserTypes.ADMINISTRATIVE,
                    role,
                    keyPair.publicKey,
                    keyPair.privateKey
            )
            AdminResponse.WalletAdmin(
                    walletId = wallet.id!!,
                    tokenWalletId = tokenWallet.id,
                    alias = tokenWallet.alias,
                    issuer = tokenWallet.party,
                    publicKey = tokenWallet.publicKey
            )
        }
    }

}
