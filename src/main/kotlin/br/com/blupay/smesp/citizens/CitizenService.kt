package br.com.blupay.smesp.citizens

import br.com.blupay.blubasemodules.identity.people.PersonCredentials
import br.com.blupay.blubasemodules.identity.people.PersonSearch
import br.com.blupay.smesp.core.drivers.EncoderManager
import br.com.blupay.smesp.core.providers.identity.IdentityProvider
import br.com.blupay.smesp.core.resources.citizens.exceptions.CitizenException
import br.com.blupay.smesp.core.resources.citizens.exceptions.CitizenNotFoundException
import br.com.blupay.smesp.core.resources.citizens.models.CitizenResponse
import br.com.blupay.smesp.core.resources.citizens.models.PasswordRequest
import br.com.blupay.smesp.core.resources.enums.OnboardFlow
import br.com.blupay.smesp.core.resources.enums.OnboardFlow.VALIDATION
import br.com.blupay.smesp.core.resources.enums.UserGroups
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service
import java.util.UUID
import javax.security.auth.login.CredentialException

@Service
class CitizenService(
        private val citizenRepository: CitizenRepository,
        private val encoderManager: EncoderManager,
        private val identityProvider: IdentityProvider
) {

    fun createCredentials(citizenId: UUID, request: PasswordRequest, jwt: Jwt): CitizenResponse {
        val token = jwt.tokenValue
        val citizen = findById(citizenId)

        if (OnboardFlow.CREDENTIALS != citizen.flow) {
            throw CitizenException("This citizen $citizenId already has a credential")
        }

        val credentialsCreated = identityProvider.createPersonCredentials(token, citizenId, PersonCredentials.Request(
                password = request.password,
                groups = listOf("${UserGroups.CITIZENS}")
        ))

        if (!credentialsCreated) {
            throw CredentialException("Citizen $citizenId credentials were not created")
        }

        val updatedCitizen = citizenRepository.save(citizen.copy(flow = VALIDATION))
        return createCitizenCryptResponse(updatedCitizen)
    }

    fun findOne(citizenId: UUID, token: Jwt?): CitizenResponse {
        val citizen = findById(citizenId)
        return createCitizenCryptResponse(citizen)
    }

    fun findOneByCpf(cpf: String, jwt: Jwt): CitizenResponse {
        val token = jwt.tokenValue
        val citizen = citizenRepository.findByCpf(cpf)

        if (citizen != null) {
            return createCitizenCryptResponse(citizen)
        }

        val personList = identityProvider.peopleSearch(token, PersonSearch.Query(register = cpf))
        val person = personList.stream().findFirst().orElseThrow { throw CitizenNotFoundException(cpf) }
        val citizenSaved = citizenRepository.save(
            Citizen(
                person.id,
                person.name,
                person.register,
                person.email,
                person.phone,
                listOf()
            )
        )
        return createCitizenCryptResponse(citizenSaved)
    }

    private fun findById(citizenId: UUID) = citizenRepository.findByIdOrNull(citizenId)
            ?: throw CitizenNotFoundException("$citizenId")

    private fun createCitizenCryptResponse(citizen: Citizen): CitizenResponse {
        return CitizenResponse(
                id = citizen.id!!,
                name = citizen.name,
                cpf = citizen.cpf,
                email = encoderManager.encrypt(citizen.email),
                phone = encoderManager.encrypt(citizen.phone),
                flow = citizen.flow
        )
    }
}
