package br.com.blupay.smesp.wallets

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface WalletRepository : JpaRepository<Wallet, UUID> {
    fun findByOwnerAndToken(owner: UUID, token: UUID): Wallet?

    fun findByToken(token: UUID): Wallet?

    fun findByOwner(owner: UUID): Wallet?
}