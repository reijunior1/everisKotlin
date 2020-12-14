package br.com.blupay.smesp.wallets

import br.com.blupay.smesp.core.resources.wallets.exceptions.WalletNotFoundException
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class WalletService(val walletRepository: WalletRepository) {
    fun findWalletByOwnerAndToken(owner: UUID, token: UUID): Wallet? {
        return walletRepository.findWalletByOwnerAndToken(owner, token)
                ?: throw WalletNotFoundException(owner.toString(), token.toString())
    }
}