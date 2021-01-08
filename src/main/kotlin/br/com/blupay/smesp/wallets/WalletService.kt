package br.com.blupay.smesp.wallets

import br.com.blupay.blubasemodules.core.models.AuthCredentials
import br.com.blupay.smesp.core.providers.token.TokenException
import br.com.blupay.smesp.core.providers.token.token.RedeemTokensRequest.Settlement
import br.com.blupay.smesp.core.providers.token.wallet.BalanceResponse
import br.com.blupay.smesp.core.providers.token.wallet.SettlementBalanceRequest.SettlementType.RECEIVER
import br.com.blupay.smesp.core.resources.shared.enums.UserTypes
import br.com.blupay.smesp.core.resources.wallets.api.WalletRead.ApprovedBalanceResponse
import br.com.blupay.smesp.core.resources.wallets.exceptions.BalanceNotFoundException
import br.com.blupay.smesp.core.resources.wallets.exceptions.WalletNotFoundException
import br.com.blupay.smesp.core.services.OwnerService
import br.com.blupay.smesp.token.TokenWalletService
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class WalletService(
    private val walletRepository: WalletRepository,
    private val tokenWalletService: TokenWalletService,
    private val ownerService: OwnerService
) {
    fun findByOwnerAndToken(owner: UUID, token: UUID): Wallet? {
        return walletRepository.findByOwnerAndToken(owner, token)
            ?: throw WalletNotFoundException(owner.toString())
    }

    fun findByOwner(owner: UUID): Wallet {
        return walletRepository.findByOwner(owner)
            ?: throw WalletNotFoundException(owner.toString())
    }

    fun findByRole(role: Wallet.Role): List<Wallet> {
        return walletRepository.findByRole(role)
            ?: throw WalletNotFoundException(role.name)
    }

    fun findByOwnerAndRole(owner: UUID, role: Wallet.Role): List<Wallet> {
        return walletRepository.findByOwnerAndRole(owner, role)
            ?: throw WalletNotFoundException("${owner}/${role.name}")
    }

    fun findByToken(token: UUID): Wallet {
        return walletRepository.findByToken(token)
            ?: throw WalletNotFoundException(token.toString())
    }

    fun findById(id: UUID): Wallet {
        return walletRepository.findById(id).orElseThrow {
            throw WalletNotFoundException(id.toString())
        }
    }

    fun getBalance(walletId: UUID, auth: AuthCredentials): BalanceResponse {
        val wallet = findById(walletId)
        ownerService.userOwns(auth, wallet.owner)

        val response = tokenWalletService.getBalance(auth.token, wallet, wallet.token).block()
            ?: throw BalanceNotFoundException(wallet.token.toString())
        return BalanceResponse(response.balance)
    }

    fun getApprovedBalanceAndReleaseDate(walletId: UUID, auth: AuthCredentials): List<ApprovedBalanceResponse> {
        val wallet = findById(walletId)
        ownerService.userOwns(auth, wallet.owner)

        return tokenWalletService.getSettlementList(auth.token, wallet, wallet.token)
            .map { setlements ->
                setlements.map { settlement: Settlement ->
                    ApprovedBalanceResponse(
                        settlement.date,
                        settlement.dueDate,
                        settlement.amount
                    )
                }
            }.block()
            ?: throw BalanceNotFoundException(wallet.token.toString())
    }

    fun save(
        owner: UUID,
        tokenId: UUID,
        type: UserTypes,
        role: Wallet.Role,
        publicKey: String,
        privateKey: String
    ): Wallet {
        return walletRepository.save(
            Wallet(
                UUID.randomUUID(),
                owner,
                tokenId,
                type,
                role, publicKey,
                privateKey,
                ""
            )
        )
    }

    fun getSettlementBalance(id: UUID, auth: AuthCredentials): BalanceResponse {
        val wallet = walletRepository.findById(id).orElseThrow { WalletNotFoundException(id.toString()) }
        ownerService.userOwns(auth, wallet.owner)
        return tokenWalletService.getSettlementBalance(auth.token, wallet, wallet.token, RECEIVER).block()
            ?: throw TokenException()
    }
}