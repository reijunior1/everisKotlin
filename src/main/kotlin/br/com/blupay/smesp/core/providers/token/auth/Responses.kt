package br.com.blupay.smesp.core.providers.token.auth

import java.time.Instant

data class AccessToken(
        val access_token: String? = null,
        val expires_in: Long = 0
) {
    private val expires: Instant = Instant.now().plusSeconds(expires_in - 60)

    fun isActive(): Boolean {
        return access_token != null && expires.toEpochMilli() > Instant.now().toEpochMilli()
    }
}