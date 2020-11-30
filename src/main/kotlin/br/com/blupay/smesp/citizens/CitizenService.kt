package br.com.blupay.smesp.citizens

import br.com.blupay.smesp.core.encoders.CryptationManager
import br.com.blupay.smesp.core.providers.identity.IdentityProvider
import org.springframework.stereotype.Service

@Service
class CitizenService(private val citizenRepository: CitizenRepository,
                     private val cryptationManager: CryptationManager,
                     private val identityProvider: IdentityProvider) {

    fun findByCpf(cpf: String, auth: String): CitizenSearch.Response {

        val citizen = citizenRepository.findByCpf(cpf)

        if (citizen != null) {
            // TODO ver quais tratativas se este person tenha o onboarded false
            return CitizenSearch.Response(id = citizen.id!!,
                    cpf = citizen.cpf,
                    email = cryptationManager.encrypt(citizen.email),
                    phone = cryptationManager.encrypt(citizen.phone),
                    onboarded = citizen.onboarded)
        } else {

            val person = identityProvider.findPersonByCpf(cpf, auth)

            val citizenSaved = citizenRepository.save(Citizen(person.id, person.cpf, person.email, person.phone, false))

            return CitizenSearch.Response(id = citizenSaved.id!!,
                    cpf = citizenSaved.cpf,
                    email = cryptationManager.encrypt(citizenSaved.email),
                    phone = cryptationManager.encrypt(citizenSaved.phone),
                    onboarded = citizenSaved.onboarded)
        }
    }
}
