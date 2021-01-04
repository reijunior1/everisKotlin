package br.com.blupay.smesp.administrative

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface AdministrativeRepository : JpaRepository<Administrative, UUID>