package br.com.blupay.smesp.wallets

import br.com.blupay.smesp.core.resources.shared.enums.UserTypes
import br.com.blupay.smesp.core.resources.wallets.exceptions.WalletNotFoundException
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class WalletService(val walletRepository: WalletRepository) {
    fun findWalletByOwnerAndToken(owner: UUID, token: UUID): Wallet? {
        return walletRepository.findWalletByOwnerAndToken(owner, token)
            ?: throw WalletNotFoundException(owner.toString())
    }

    fun findWalletByOwner(owner: UUID): Wallet {
        return walletRepository.findWalletByOwner(owner)
            ?: throw WalletNotFoundException(owner.toString())
    }

    fun findByToken(token: UUID): Wallet {
        return walletRepository.findWalletByToken(token)
            ?: throw WalletNotFoundException(token.toString())
    }

    fun findWalletById(id: UUID): Wallet {
        return walletRepository.findById(id).orElseThrow {
            throw WalletNotFoundException(id.toString())
        }
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