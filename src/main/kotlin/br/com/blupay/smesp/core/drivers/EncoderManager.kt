package br.com.blupay.smesp.core.drivers

import org.apache.tomcat.util.codec.binary.Base64
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

@Component
data class EncoderManager(
        @Value("\${encoder.key}") private val secret: String,
        private val algorithm: String = "Blowfish",
        private val pKey: Key = SecretKeySpec(secret.toByteArray(), algorithm),
        private val cipher: Cipher = Cipher.getInstance(algorithm)
) {

    fun encrypt(input: String? = null): String? {
        val inputBytes = input?.toByteArray()
        return if (input != null) inputBytes?.let { encrypt(it) } else null
    }

    fun encrypt(inputBytes: ByteArray): String {
        cipher.init(Cipher.ENCRYPT_MODE, pKey)
        val encryptionBytes = cipher.doFinal(inputBytes)
        return Base64.encodeBase64String(encryptionBytes)
    }

    fun decrypt(encryptionStr: String): String {
        val decryptedBytes = Base64.decodeBase64(encryptionStr)
        return decrypt(decryptedBytes)
    }

    fun decrypt(encryptionBytes: ByteArray): String {
        cipher.init(Cipher.DECRYPT_MODE, pKey)
        val decrypt = cipher.doFinal(encryptionBytes)
        return String(decrypt)
    }
}