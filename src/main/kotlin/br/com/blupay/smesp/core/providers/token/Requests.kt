package br.com.blupay.smesp.core.providers.token

import java.time.Instant
import java.util.UUID

data class TokenHeader(
        var hash: String? = null,
        var date: Long? =  null,
        val expiration: Long? = Instant.now().plusSeconds(60 * 60 * 24).toEpochMilli(), // TODO: definir um tempo aceitável
        val action: String,
        val wallet: UUID
)

data class LocalWalletRequest(
        val id: UUID
)

data class WalletRequest(
        val id: UUID,
        val issuer: String // PublicKey (Base64)
)

data class TokenRequest(
        val issuer: String? = null, // PublicKey (Base64)
        val type: String,
        val quantity: Long? = null,
        val maxSize: Long? = null
)

data class Sign(
        val data: Any,
        val privateKey: String,
        val publicKey: String
)

