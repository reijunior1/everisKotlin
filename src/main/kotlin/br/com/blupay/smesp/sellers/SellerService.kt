package br.com.blupay.smesp.sellers

import br.com.blupay.blubasemodules.identity.people.PersonCredentials
import br.com.blupay.blubasemodules.identity.people.PersonSearch
import br.com.blupay.smesp.core.drivers.EncoderManager
import br.com.blupay.smesp.core.providers.identity.IdentityProvider
import br.com.blupay.smesp.core.resources.citizens.models.PasswordRequest
import br.com.blupay.smesp.core.resources.enums.OnboardFlow
import br.com.blupay.smesp.core.resources.enums.OnboardFlow.VALIDATION
import br.com.blupay.smesp.core.resources.enums.UserGroups
import br.com.blupay.smesp.core.resources.sellers.exceptions.SellerException
import br.com.blupay.smesp.core.resources.sellers.exceptions.SellerNotFoundException
import br.com.blupay.smesp.core.resources.sellers.models.SellerResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service
import java.util.UUID
import javax.security.auth.login.CredentialException

@Service
class SellerService(private val sellerRepository: SellerRepository,
                    private val identityProvider: IdentityProvider,
                    private val encoderManager: EncoderManager) {

    fun createCredentials(sellerId: UUID, request: PasswordRequest, jwt: Jwt): SellerResponse {
        val token = jwt.tokenValue
        val seller = findById(sellerId)

        if (OnboardFlow.CREDENTIALS != seller.flow) {
            throw SellerException("This seller $sellerId already has a credential")
        }

        val credentialsCreated = identityProvider.createPersonCredentials(token, sellerId, PersonCredentials.Request(
                password = request.password,
                groups = listOf("${UserGroups.SELLERS}")
        ))

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
        val savedSeller = sellerRepository.save(Seller(id = person.id, name = person.name, cnpj = person.register, email = person.email, phone = person.phone))
        return createSellerCryptResponse(savedSeller)

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

    private fun findById(sellerId: UUID) = sellerRepository.findByIdOrNull(sellerId)
            ?: throw SellerNotFoundException("$sellerId")
}