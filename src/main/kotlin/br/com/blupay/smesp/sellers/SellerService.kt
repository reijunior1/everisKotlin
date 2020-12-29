package br.com.blupay.smesp.sellers

import br.com.blupay.blubasemodules.identity.people.PersonCredentials
import br.com.blupay.blubasemodules.identity.people.PersonSearch
import br.com.blupay.smesp.core.drivers.EncoderManager
import br.com.blupay.smesp.core.providers.identity.IdentityProvider
import br.com.blupay.smesp.core.providers.token.wallet.IssueWallet
import br.com.blupay.smesp.core.providers.token.wallet.WalletRole.RECEIVER
import br.com.blupay.smesp.core.resources.citizens.models.PasswordRequest
import br.com.blupay.smesp.core.resources.enums.OnboardFlow
import br.com.blupay.smesp.core.resources.enums.OnboardFlow.VALIDATION
import br.com.blupay.smesp.core.resources.enums.UserGroups
import br.com.blupay.smesp.core.resources.enums.UserTypes.SELLER
import br.com.blupay.smesp.core.resources.sellers.exceptions.SellerException
import br.com.blupay.smesp.core.resources.sellers.exceptions.SellerNotFoundException
import br.com.blupay.smesp.core.resources.sellers.models.BankResponse
import br.com.blupay.smesp.core.resources.sellers.models.SellerResponse
import br.com.blupay.smesp.core.services.JwsService
import br.com.blupay.smesp.token.TokenWalletService
import br.com.blupay.smesp.wallets.Wallet
import br.com.blupay.smesp.wallets.WalletRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service
import java.util.UUID
import javax.security.auth.login.CredentialException

@Service
class SellerService(
    private val sellerRepository: SellerRepository,
    private val identityProvider: IdentityProvider,
    private val encoderManager: EncoderManager,
    private val tokenWalletService: TokenWalletService,
    private val walletRepository: WalletRepository,
    private val jwsService: JwsService
) {

    fun createCredentials(sellerId: UUID, request: PasswordRequest, jwt: Jwt): SellerResponse {
        val token = jwt.tokenValue
        val seller = findById(sellerId)

        if (OnboardFlow.CREDENTIALS != seller.flow) {
            throw SellerException("This seller $sellerId already has a credential")
        }

        val credentialsCreated = identityProvider.createPersonCredentials(
            token, sellerId, PersonCredentials.Request(
                password = request.password,
                groups = listOf("${UserGroups.SELLERS}")
            )
        )

        if (!credentialsCreated) {
            throw CredentialException("Citizen $sellerId credentials were not created")
        }

        val updatedSeller = sellerRepository.save(seller.copy(flow = VALIDATION))
        return createSellerCryptResponse(updatedSeller)
    }

    fun findOneByCnpj(cnpj: String, jwt: Jwt): SellerResponse {

        val token = jwt.tokenValue
        val seller = sellerRepository.findByCnpj(cnpj)

        if (seller != null) {
            return createSellerCryptResponse(seller)
        }

        val sellerList = identityProvider.peopleSearch(token, PersonSearch.Query(register = cnpj))

        val person = sellerList.stream().findFirst().orElseThrow { throw SellerNotFoundException(cnpj) }
        val sellerSaved = sellerRepository.save(
            Seller(
                id = person.id,
                name = person.name,
                cnpj = person.register,
                email = person.email,
                phone = person.phone,
                banks = listOf()
            )
        )

        val pairKeys = jwsService.getKeyPair()

        val wallet = tokenWalletService.issueWallet(
            jwt.tokenValue,
            IssueWallet(sellerSaved.id.toString(), pairKeys.publicKey.toString(), RECEIVER)
        ).block()
        walletRepository.save(
            Wallet(
                UUID.randomUUID(),
                sellerSaved.id!!,
                wallet?.id!!,
                SELLER,
                pairKeys.publicKey.toString(),
                pairKeys.privateKey.toString()
            )
        )

        return createSellerCryptResponse(sellerSaved)

    }

    fun findById(sellerId: UUID) = sellerRepository.findByIdOrNull(sellerId)
        ?: throw SellerNotFoundException("$sellerId")

    fun findBankAccounts(sellerId: UUID): List<BankResponse> {
        val banks = findById(sellerId).banks ?: listOf()
        return banks.map {
            BankResponse(
                    it.id,
                    it.name,
                    it.cnpj,
                    it.agency,
                    it.account,
                    it.pix
            )
        }
    }

    private fun createSellerCryptResponse(seller: Seller): SellerResponse {
        return SellerResponse(
                id = seller.id!!,
                name = seller.name,
                cnpj = seller.cnpj,
                email = encoderManager.encrypt(seller.email),
                phone = encoderManager.encrypt(seller.phone),
                flow = seller.flow
        )
    }
}