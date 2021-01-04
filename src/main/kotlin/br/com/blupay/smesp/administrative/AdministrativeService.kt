package br.com.blupay.smesp.administrative

import br.com.blupay.smesp.core.providers.token.wallet.IssueWallet
import br.com.blupay.smesp.core.providers.token.wallet.WalletRole
import br.com.blupay.smesp.core.resources.administrative.exceptions.AdministrativeEntitiesAlreadyExistException
import br.com.blupay.smesp.core.resources.administrative.models.WalletAdminResponse
import br.com.blupay.smesp.core.resources.shared.enums.UserTypes
import br.com.blupay.smesp.core.services.JwsService
import br.com.blupay.smesp.token.TokenWalletService
import br.com.blupay.smesp.wallets.WalletService
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
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
    fun create(token: String): Mono<List<WalletAdminResponse>> = createEntity(token, "admin", WalletRole.ADMIN, true)
            .flatMap { adminWallet ->

                val cashinWallet = createEntity(token, "cashin", WalletRole.CASHIN) // não está transferindo para PAYER - mudar
                val cashoutWallet = createEntity(token, "cashout", WalletRole.CASHOUT) // precisa ser receiver - mmudar
                val ioyWallet = createEntity(token, "ioy", WalletRole.IOY) // precisa ser payer PAYER - mudar

                Mono.zip(cashinWallet, cashoutWallet, ioyWallet)
                        .map { (cashinWallet, cashoutWallet, ioyWallet) -> arrayOf(adminWallet, cashinWallet, cashoutWallet, ioyWallet) }
                        .map {
                            it.toList()
                        }
            }

    private fun createEntity(token: String, alias: String, role: WalletRole, first: Boolean = false): Mono<WalletAdminResponse> {
        if (first && repository.count() > 0) throw AdministrativeEntitiesAlreadyExistException()

        val administrative = Administrative(
                UUID.randomUUID(),
                name = alias
        )
        val entity = repository.save(administrative)
        return createWallet(token, entity.id, alias, role)
    }

    private fun createWallet(token: String, administrativeId: UUID, alias: String, role: WalletRole): Mono<WalletAdminResponse> {
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
                    keyPair.publicKey,
                    keyPair.privateKey
            )
            WalletAdminResponse(
                    administrativeId = administrativeId,
                    walletId = wallet.id!!,
                    tokenWalletId = tokenWallet.id,
                    alias = tokenWallet.alias,
                    issuer = tokenWallet.party,
                    publicKey = tokenWallet.publicKey
            )
        }
    }

}
