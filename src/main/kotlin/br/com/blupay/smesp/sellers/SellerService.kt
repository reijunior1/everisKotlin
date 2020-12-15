package br.com.blupay.smesp.sellers

import br.com.blupay.blubasemodules.identity.people.PersonSearch
import br.com.blupay.smesp.core.drivers.EncoderManager
import br.com.blupay.smesp.core.providers.identity.IdentityProvider
import br.com.blupay.smesp.core.resources.sellers.exceptions.SellerNotFoundException
import br.com.blupay.smesp.core.resources.sellers.models.SellerResponse
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service

@Service
class SellerService(private val sellerRepository: SellerRepository,
                    private val identityProvider: IdentityProvider,
                    private val encoderManager: EncoderManager) {
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
}