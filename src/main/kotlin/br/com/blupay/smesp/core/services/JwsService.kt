package br.com.blupay.smesp.core.services

import com.google.gson.Gson
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.JWSObject
import com.nimbusds.jose.JWSSigner
import com.nimbusds.jose.Payload
import com.nimbusds.jose.crypto.RSASSASigner
import com.nimbusds.jose.jwk.KeyUse
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator
import org.springframework.stereotype.Service
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64
import java.util.UUID

@Service
class JwsService {

    fun getKeyPairEncoded(): KeyPairEncoded {
        val keyPair = getKeyPair()
        val encoder = Base64.getEncoder()
        val sk = encoder.encodeToString(keyPair.privateKey.encoded)
        val pk = encoder.encodeToString(keyPair.publicKey.encoded)
        return KeyPairEncoded(sk, pk)
    }

    fun sign(data: Any, privateKey: String, publicKey: String): String {
        val rsaJWK = getRSAKey(publicKey, privateKey)
        val signer: JWSSigner = RSASSASigner(rsaJWK)

        val jsonData = Gson().toJson(data)

        var jwsObject = JWSObject(
                JWSHeader.Builder(JWSAlgorithm.RS256).keyID(rsaJWK.keyID).build(),
                Payload(jsonData))

        jwsObject.sign(signer)
        return jwsObject.serialize()
    }

    fun getPublicKey(publicKey: PublicKey): String {
        val encoder = Base64.getEncoder()
        return encoder.encodeToString(publicKey.encoded)
    }

    fun getPublicKey(publicKey: String): PublicKey {
        val decoder = Base64.getDecoder()
        val pkBytes = decoder.decode(publicKey)

        val pk = X509EncodedKeySpec(pkBytes)
        val kf = KeyFactory.getInstance("RSA")
        return kf.generatePublic(pk)
    }

    private fun getRSAKey(publicKey: String, privateKey: String? = null): RSAKey {
        val pubKey = getPublicKey(publicKey)

        val rsaJWK = RSAKey.Builder(pubKey as RSAPublicKey)

        if (privateKey != null) {
            val privKey = getPrivateKey(privateKey)
            rsaJWK.privateKey(privKey as RSAPrivateKey)
                    .keyUse(KeyUse.SIGNATURE)
        }

        return rsaJWK.build()
    }

    private fun getPrivateKey(privateKey: String): PrivateKey {
        val decoder = Base64.getDecoder()
        val privateKeyBytes = decoder.decode(privateKey)

        val sk = PKCS8EncodedKeySpec(privateKeyBytes)
        val kf = KeyFactory.getInstance("RSA")
        return kf.generatePrivate(sk)
    }

    private fun getKeyPair(): KeyPair {
        val rsaJWK: RSAKey = RSAKeyGenerator(2048)
                .keyID(UUID.randomUUID().toString())
                .generate()
        return KeyPair(rsaJWK.toRSAPrivateKey(), rsaJWK.toRSAPublicKey())
    }

    class KeyPair(
            val privateKey: PrivateKey,
            val publicKey: PublicKey
    )

    class KeyPairEncoded(
            val privateKey: String,
            val publicKey: String
    )
}