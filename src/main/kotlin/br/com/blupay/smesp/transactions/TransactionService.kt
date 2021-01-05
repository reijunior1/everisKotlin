package br.com.blupay.smesp.transactions

import br.com.blupay.smesp.citizens.CitizenService
import br.com.blupay.smesp.core.resources.transactions.models.PaymentResponse
import br.com.blupay.smesp.sellers.SellerService
import br.com.blupay.smesp.token.Creditor
import br.com.blupay.smesp.token.TokenService
import br.com.blupay.smesp.wallets.Wallet
import br.com.blupay.smesp.wallets.WalletService
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.time.Instant
import java.time.LocalDateTime
import java.util.TimeZone
import java.util.UUID

@Service
class TransactionService(
        private val repository: TransactionRepository,
        private val sellerService: SellerService,
        private val citizenService: CitizenService,
        private val walletService: WalletService,
        private val tokenService: TokenService
) {
    fun paymentByPin(token: String, creditorDocument: String, debtorId: UUID, amount: Long, pin: String): Mono<PaymentResponse> {
        val creditor = sellerService.findByCnpj(creditorDocument)

        val creditorWallet = walletService.findByOwner(creditor.id!!)
        val ioyWallet = walletService.findByRole(Wallet.Role.IOY).first()
        val debtorWallet = walletService.findById(debtorId)

        // TODO: validar pin e testar a transferência

        return tokenService.safeMoveToken(
                token,
                debtorWallet,
                Instant.now().toEpochMilli(),
                debtorWallet.token,
                ioyWallet.token,
                listOf(
                        Creditor(
                                creditorWallet.token,
                                amount
                        )
                )
        ).map {
            val date = LocalDateTime.ofInstant(Instant.ofEpochMilli(it.header.date!!), TimeZone.getDefault().toZoneId())
            val transaction = repository.save(
                    Transaction(
                            UUID.randomUUID(),
                            it.header.hash!!,
                            date,
                            debtorWallet,
                            creditorWallet,
                            amount,
                            Transaction.TransactionType.SAFE_MOVE_CREDITOR
                    )
            )
            PaymentResponse(
                    transaction
            )
        }
    }
}