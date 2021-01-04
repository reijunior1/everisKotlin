package br.com.blupay.smesp.core.services

import br.com.blupay.blubasemodules.core.models.AuthCredentials
import br.com.blupay.smesp.citizens.CitizenRepository
import br.com.blupay.smesp.core.resources.shared.enums.UserTypes
import br.com.blupay.smesp.core.resources.shared.exceptions.UserNotFoundException
import br.com.blupay.smesp.core.resources.shared.exceptions.UserPermissionException
import br.com.blupay.smesp.core.resources.shared.models.Owner
import br.com.blupay.smesp.sellers.SellerRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class OwnerService(
        private val sellerRepository: SellerRepository,
        private val citizenRepository: CitizenRepository
) {
    fun getOwner(username: String): Owner {
        return if (username.length > 11) {
            val seller = sellerRepository.findByCnpj(username) ?: throw UserNotFoundException(username)
            Owner(seller.id!!, seller.cnpj, UserTypes.SELLER)
        } else {
            val citizen = citizenRepository.findByCpf(username) ?: throw UserNotFoundException(username)
            Owner(citizen.id!!, citizen.cpf, UserTypes.CITIZEN)
        }
    }

    fun userOwns(auth: AuthCredentials, resourceOwnerId: UUID) {
        val owner = getOwner(auth.username)
        if (owner.id != resourceOwnerId) {
            throw UserPermissionException("User ${auth.username} cannot access this resource.")
        }
    }
}