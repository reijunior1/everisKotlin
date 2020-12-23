package br.com.blupay.smesp.children

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ChildRepository : JpaRepository<Child, UUID> {
}