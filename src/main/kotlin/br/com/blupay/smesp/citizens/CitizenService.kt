package br.com.blupay.smesp.citizens

import br.com.blupay.smesp.core.providers.identity.IdentityProvider
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class CitizenService(private val citizenRepository: CitizenRepository,
                     private val passwordEncoder: BCryptPasswordEncoder,
                     private val identityProvider: IdentityProvider) {
    fun findByCpf(cpf: String, auth: String): CitizenSearch.Response {

        val citizen = citizenRepository.findByCpf(cpf)

        if (citizen != null) {
            // TODO ver quais tratativas se este person tenha o onboarded false
            return CitizenSearch.Response(id = citizen.id!!,
                    cpf = passwordEncoder.encode(citizen.cpf),
                    email = passwordEncoder.encode(citizen.email),
                    phone = citizen.phone,
                    onboarded = citizen.onboarded)
        } else {

            val person = identityProvider.findPersonByCpf(cpf, auth)

            val citizenSaved = citizenRepository.save(Citizen(person.id, person.cpf, person.email, person.phone, false))

            return CitizenSearch.Response(id = citizenSaved.id!!,
                    cpf = passwordEncoder.encode(citizenSaved.cpf),
                    email = passwordEncoder.encode(citizenSaved.email),
                    phone = citizenSaved.phone,
                    onboarded = citizenSaved.onboarded)
        }
    }
}
