package br.com.blupay.smesp.sellers

import br.com.blupay.blubasemodules.core.models.AuthCredentials
import br.com.blupay.blubasemodules.identity.people.PersonCreate
import br.com.blupay.blubasemodules.identity.people.PersonCredentials
import br.com.blupay.smesp.core.drivers.EncoderManager
import br.com.blupay.smesp.core.providers.identity.IdentityProvider
import br.com.blupay.smesp.core.resources.sellers.api.SellerBankAccount
import br.com.blupay.smesp.core.resources.sellers.exceptions.SellerException
import br.com.blupay.smesp.core.resources.sellers.exceptions.SellerNotFoundException
import br.com.blupay.smesp.core.resources.sellers.models.BankResponse
import br.com.blupay.smesp.core.resources.sellers.models.SellerResponse
import br.com.blupay.smesp.core.resources.shared.enums.OnboardFlow
import br.com.blupay.smesp.core.resources.shared.enums.OnboardFlow.VALIDATION
import br.com.blupay.smesp.core.resources.shared.enums.UserGroups
import br.com.blupay.smesp.core.resources.shared.models.PasswordRequest
import br.com.blupay.smesp.core.services.OwnerService
import br.com.blupay.smesp.sellers.banks.BankAccount
import br.com.blupay.smesp.sellers.banks.BankAccountRepository
import br.com.blupay.smesp.wallets.WalletRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.UUID
import javax.security.auth.login.CredentialException

@Service
class SellerService(
    private val bankAccountRepository: BankAccountRepository,
    private val sellerRepository: SellerRepository,
    private val identityProvider: IdentityProvider,
    private val encoderManager: EncoderManager,
    private val walletRepository: WalletRepository,
    private val ownerService: OwnerService
) {

    private val logger: Logger = LoggerFactory.getLogger(SellerService::class.java)

    fun createCredentials(sellerId: UUID, request: PasswordRequest, auth: AuthCredentials): Mono<SellerResponse> {
        logger.info("Creating a new user identity credentials from seller")
        val seller = findById(sellerId)

        if (OnboardFlow.CREDENTIALS != seller.flow) {
            throw SellerException("This seller $sellerId already has a credential")
        }

        return identityProvider.createPerson(
            auth.token, PersonCreate.Request(
                id = seller.id,
                name = seller.name,
                register = seller.cnpj,
                email = seller.email,
                phone = seller.phone,
            )
        ).zipWhen {
            logger.info("Creating user password")
            identityProvider.createPersonCredentials(
                auth.token, sellerId, PersonCredentials.Request(
                    password = request.password,
                    groups = listOf("${UserGroups.SELLERS}")
                )
            )
        }.map { tuple ->
            logger.info("Check if password is valid")
            val passwordCreated = tuple.t2
            if (!passwordCreated) throw CredentialException("Seller $sellerId credentials were not created")
            tuple.t1
        }.map {
            val updatedSeller = sellerRepository.save(seller.copy(flow = VALIDATION))
            createSellerCryptResponse(updatedSeller)
        }
    }

    fun findOneByCnpj(cnpj: String, jwt: Jwt): SellerResponse {

        val seller = sellerRepository.findByCnpj(cnpj)
            ?: throw SellerNotFoundException(cnpj)

        return createSellerCryptResponse(seller)
    }

    fun findOne(sellerId: UUID, auth: AuthCredentials): SellerResponse {
        ownerService.userOwns(auth, sellerId)

        val seller = findByCnpj(auth.username)
        val wallet = walletRepository.findByOwner(seller.id!!)
        return createSellerCryptResponse(seller, wallet?.id!!)
    }

    fun findById(sellerId: UUID) = sellerRepository.findByIdOrNull(sellerId)
        ?: throw SellerNotFoundException("$sellerId")

    fun createBankAccount(sellerId: UUID, requestBody: SellerBankAccount.Request): BankResponse? {
        val seller = findById(sellerId)
        val bankAccountSaved = bankAccountRepository.save(
            BankAccount(
                name = requestBody.name,
                cnpj = seller.cnpj,
                agency = requestBody.agency,
                account = requestBody.account,
                seller = seller,
                pix = requestBody.pix
            )
        )
        return createBankResponse(bankAccountSaved)
    }

    fun findBankAccounts(sellerId: UUID): List<BankResponse> {
        val banks = findById(sellerId).banks ?: listOf()
        return banks.map { createBankResponse(it) }
    }

    fun findByCnpj(cnpj: String): Seller {
        return sellerRepository.findByCnpj(cnpj)
            ?: throw SellerNotFoundException(cnpj)
    }

    private fun createBankResponse(bankAccount: BankAccount) = BankResponse(
        bankAccount.id,
        bankAccount.name,
        bankAccount.cnpj,
        bankAccount.agency,
        bankAccount.account,
        bankAccount.pix
    )

    private fun createSellerCryptResponse(seller: Seller, walletId: UUID? = null): SellerResponse {
        return SellerResponse(
            id = seller.id!!,
            name = seller.name,
            cnpj = seller.cnpj,
            email = encoderManager.encrypt(seller.email),
            phone = encoderManager.encrypt(seller.phone),
            flow = seller.flow,
            walletId = walletId
        )
    }
}