package br.com.blupay.smesp.wallets

import br.com.blupay.smesp.core.resources.shared.enums.UserTypes
import br.com.blupay.blubasemodules.core.extensions.username
import br.com.blupay.blubasemodules.core.models.AuthCredentials
import br.com.blupay.smesp.core.providers.token.wallet.BalanceResponse
import br.com.blupay.smesp.core.resources.wallets.exceptions.BalanceNotFoundException
import br.com.blupay.smesp.core.resources.wallets.exceptions.WalletNotFoundException
import br.com.blupay.smesp.core.resources.wallets.exceptions.WalletOwnerException
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

    fun findByToken(token: UUID): Wallet {
        return walletRepository.findByToken(token)
            ?: throw WalletNotFoundException(token.toString())
    }

    fun findById(id: UUID): Wallet {
        return walletRepository.findById(id).orElseThrow {
            throw WalletNotFoundException(id.toString())
        }
    }

    fun getBalance(id: UUID, auth: AuthCredentials): BalanceResponse {
        val ownerId = ownerService.getOwner(auth.username)!!
        val wallet = walletRepository.findByOwner(ownerId)
            ?: throw WalletNotFoundException(ownerId.toString())

        if (wallet.id.toString() != id.toString()) {
            throw WalletOwnerException(auth.username)
        }

        val balance = tokenWalletService.getBalance(auth.token, wallet, wallet.token).block()
            ?: throw BalanceNotFoundException(wallet.token.toString())
        return BalanceResponse(balance.balance)
    }

    fun save(owner: UUID, tokenId: UUID, type: UserTypes, publicKey: String, privateKey: String): Wallet {
        return walletRepository.save(
                Wallet(
                        UUID.randomUUID(),
                        owner,
                        tokenId,
                        type,
                        publicKey,
                        privateKey
                )
        )
    }
}