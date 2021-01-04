package br.com.blupay.smesp.core.services

import br.com.blupay.smesp.citizens.CitizenRepository
import br.com.blupay.smesp.sellers.SellerRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class OwnerService(
    private val sellerRepository: SellerRepository,
    private val citizenRepository: CitizenRepository
) {
    fun getOwner(username: String): UUID? {
        return if (username.length > 11) sellerRepository.findByCnpj(username)?.id
        else citizenRepository.findByCpf(username)?.id
    }
}