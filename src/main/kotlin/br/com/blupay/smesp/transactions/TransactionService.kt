package br.com.blupay.smesp.transactions

import br.com.blupay.smesp.administrative.AdministrativeService
import br.com.blupay.smesp.citizens.CitizenService
import br.com.blupay.smesp.core.resources.transactions.models.CashinResponse
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
        private val administrativeService: AdministrativeService,
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

        val debtor = citizenService.findById(debtorWallet.owner)

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
            val transaction = save(
                    id = UUID.randomUUID(),
                    hash = it.header.hash!!,
                    date = date,
                    debtorName = debtor.name,
                    debtor = debtorWallet,
                    creditorName = creditor.name,
                    creditor = creditorWallet,
                    amount = amount,
                    type = Transaction.Type.SAFE_MOVE_CREDITOR,
                    status = Transaction.Status.NEW
            )
            PaymentResponse(
                    transaction
            )
        }
    }

    fun cashin(token: String, document: String, amount: Long): Mono<CashinResponse> {
        val administrative = administrativeService.findByDocument(document)
        val adminWallet = walletService.findByOwnerAndRole(administrative.id, Wallet.Role.ADMIN).first()
        val cashinWallet = walletService.findByOwnerAndRole(administrative.id, Wallet.Role.CASHIN).first()

        return tokenService.issueToken(
                token,
                adminWallet,
                cashinWallet.token,
                amount,
                amount / 1000
        ).map {
            val date = LocalDateTime.ofInstant(Instant.ofEpochMilli(it.header.date!!), TimeZone.getDefault().toZoneId())
            val transaction = save(
                    id = UUID.randomUUID(),
                    hash = it.header.hash!!,
                    date = date,
                    creditorName = administrative.name,
                    creditor = cashinWallet,
                    amount = amount,
                    type = Transaction.Type.CASHIN,
                    status = Transaction.Status.NEW
            )
            CashinResponse(
                    transaction
            )
        }
    }

    fun save(
            id: UUID,
            hash: String,
            date: LocalDateTime,
            debtorName: String? = null,
            debtor: Wallet? = null,
            creditorName: String? = null,
            creditor: Wallet? = null,
            amount: Long,
            type: Transaction.Type,
            receipt: String? = null,
            status: Transaction.Status
    ): Transaction {
        return repository.save(
                Transaction(
                        id = id,
                        hash = hash,
                        date = date,
                        debtorName = debtorName,
                        debtorWallet = debtor,
                        creditorName = creditorName,
                        creditorWallet = creditor,
                        amount = amount,
                        type = type,
                        receipt = receipt,
                        status = status
                )
        )
    }
}