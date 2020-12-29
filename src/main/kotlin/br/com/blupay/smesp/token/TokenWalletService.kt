package br.com.blupay.smesp.token

import br.com.blupay.smesp.core.providers.token.LocalWalletRequest
import br.com.blupay.smesp.core.providers.token.TokenHeader
import br.com.blupay.smesp.core.providers.token.TokenRequest
import br.com.blupay.smesp.core.providers.token.node.NodeProvider
import br.com.blupay.smesp.core.providers.token.wallet.AddAndRemoveRoleRequest
import br.com.blupay.smesp.core.providers.token.wallet.BalanceRequest
import br.com.blupay.smesp.core.providers.token.wallet.BalanceResponse
import br.com.blupay.smesp.core.providers.token.wallet.GetWalletRequest
import br.com.blupay.smesp.core.providers.token.wallet.IoyBalanceRequest
import br.com.blupay.smesp.core.providers.token.wallet.IssueWallet
import br.com.blupay.smesp.core.providers.token.wallet.SettlementBalanceRequest
import br.com.blupay.smesp.core.providers.token.wallet.WalletProvider
import br.com.blupay.smesp.core.providers.token.wallet.WalletResponse
import br.com.blupay.smesp.core.providers.token.wallet.WalletRole
import br.com.blupay.smesp.core.providers.token.wallet.WalletTokenResponse
import br.com.blupay.smesp.core.services.JwsService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.UUID

@Service
class TokenWalletService(
        @Value("service.token.type") private val tokenType: String,
        private val walletProvider: WalletProvider,
        private val nodeProvider: NodeProvider
) {
    fun loadAdmin(token: String): Mono<WalletTokenResponse> {
        // TODO: criar um novo par de chaves
        //val keyPair = jwsService.getKeyPairEncoded()
        val keyPair = JwsService.KeyPairEncoded(
            privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC2Avgnsq/HxlzjGoMG70UUP/gQBrWypIeed6DgbVL3w1pbUXIyrJcCc23Cx+/LhbAOR9uAkzptwQ1xGOC7WkHwV8cPmQz6W1EWNkIzHSseqaauraM3ve9Ke4faJTZP1/YEUzRPzdzhUK/aXLBcC4OWM9mJHwVj15C+i1yBU0vBiXhHjZC0jf3fy3F5Niol7fiIvN0TSVxyiq0PMpPA1HIASQaT4p/63vqbagD1Bj7+45mZOKE/R5OLCrSzP79b00weyZUikpocd1yM8cCo19797serSAxoECWHM84q7gmh03VIQnheXjX0oUq/YgwZJ5n7ByOkKkegKgXJDnDWDWGlAgMBAAECggEAbBy3al0pLHEXG47T5XqafbzBjENwCfzQ0cAdPzGw5pqTQUrPmmoKCNqnh9tbBHkUamckausI+ciCQ3uTgbXLFQgOpaUzoEI9mAQCrnnU8xGygG+DU8juKj/WIi2YZTTSXR9PI1gUq4FR81K7LvFn6ABEZr6WZQfWYlhFSkw/42TRVc8u4mpoKwRyVGFrpGiHm4Kt/k/ioEQRAir7kBBnULGx+/8h0BK34ZpPNW3XTwvx3O8D9WDknWXrIQyCL0hzo3L42b4YA8dAGMrheCj7cdQwgDDHnl8AfQ8f5Sd0ShhehueGtWSME60y4KmPHfDvbezk/6UUNkTiquCxqunuwQKBgQD6eMspgrkoyva9LRK7LZEmYJ6vCkdn0PjeKmXQkNaujXb5473UhOGVB5m2QySy4XvMT7nEOW1jB+GyN4ewy4Kl+J/Vb9+sRHzgoV5tFGnopF8emVX864Bt54NcMlR7649FBxW4o6OFu8WfyZ5HOLsniVkcn57Cnx8bZjTWQ37NYwKBgQC6B11Bg/JvTE3Ub8iF+ZkIbHFzlDo99+k8zrrj1pS0mvSLSGO7DUQoT53i2DNTGyypjyU1zK+dbRmXbcebsPft752eySua0Bd/0CVo0HD9bwdHtoBENRLUKg5SRZKMBK1aCBsWc2vedvtmr7Yyd2nwZlcYt6sPZhlgjLSW0D6nVwKBgBMg2HsiFZvs7C0EXTLYRwaESHobsdpW1jq5SC9GcG7CNPzejKfHQicMfoux0wKqP7cZu5klvZnbhzwCC6XP1Plx5Fr7gTx5uoO5Lynrbi8rJmyCz1NSdOtvy9NlsJgKJFXkNZ91N87FyVDrp9/OkFRzo8+QzSIV3t1Lz+sxGmT7AoGBAKCx49jIRg/mdgTgWvOAXLVuF8KjdHw7PvsMCVzu653njP8UFTJrj0jTHavq3ssXWklOIfKLUdhHeifY6OiQwlqC9kFHvacJwoLiJq6YAEKsOjm0aLCCuDUV7zEacDELiUOQZPXL1o5hcWGOv3Bwvs6vWOU9I0M2Cw3f/c+ZK8YZAoGAJlM8BMfOFN15zG6UjwDL+N4wNJtcYCVelV73XqdezvqOGJ/NbjBc9XSQMiSD46LyrK1rkz/2YgsN9L1oV9jlKjodPgTTzO54AwC8SguYzfj/ZTYNi4EHxJX1s9J9NE/CKFRcp1/9u0pwCPy2N5LLK29+al0MOGqzFRCur+HDHvk=",
            publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtgL4J7Kvx8Zc4xqDBu9FFD/4EAa1sqSHnneg4G1S98NaW1FyMqyXAnNtwsfvy4WwDkfbgJM6bcENcRjgu1pB8FfHD5kM+ltRFjZCMx0rHqmmrq2jN73vSnuH2iU2T9f2BFM0T83c4VCv2lywXAuDljPZiR8FY9eQvotcgVNLwYl4R42QtI3938txeTYqJe34iLzdE0lccoqtDzKTwNRyAEkGk+Kf+t76m2oA9QY+/uOZmTihP0eTiwq0sz+/W9NMHsmVIpKaHHdcjPHAqNfe/e7Hq0gMaBAlhzPOKu4JodN1SEJ4Xl419KFKv2IMGSeZ+wcjpCpHoCoFyQ5w1g1hpQIDAQAB"
        )

        val wallet = IssueWallet(
            "admin",
            keyPair.publicKey,
            WalletRole.ADMIN
        )

        return walletProvider.issueWallet(token, wallet).map {
            // TODO: save wallet
            it
        }
    }

    fun issueWallet(token: String, wallet: IssueWallet): Mono<WalletTokenResponse> =
        walletProvider.issueWallet(token, wallet)

    fun addWalletRole(token: String, signer: WalletTemp, id: UUID, role: WalletRole): Mono<AddAndRemoveRoleRequest> {
        val addData = AddAndRemoveRoleRequest(
            TokenHeader(
                action = "br.com.blupay.token.workflows.wallet.AddWalletRole",
                wallet = signer.id
            ),
            LocalWalletRequest(id),
            role
        )
        return walletProvider.addRole(token, addData, signer.privateKey, signer.publicKey)
    }

    fun getWallet(token: String, signer: WalletTemp, id: UUID): Mono<WalletResponse> {
        val addData = GetWalletRequest(
                TokenHeader(
                        action = "br.com.blupay.token.workflows.wallet.GetWallet",
                        wallet = signer.id
                ),
                LocalWalletRequest(id)
        )
        return walletProvider.getWallet(token, addData, signer.privateKey, signer.publicKey)
    }

    fun getBalance(token: String, signer: WalletTemp, id: UUID): Mono<BalanceResponse> {
        return nodeProvider.getPublicKey(token).map { node ->
            BalanceRequest(
                    TokenHeader(
                            action = "br.com.blupay.token.workflows.token.Balance",
                            wallet = signer.id
                    ),
                    LocalWalletRequest(id),
                    TokenRequest(
                            type = tokenType,
                            issuer = node.publicKey
                    )
            )
        }.flatMap { data ->
            walletProvider.balance(token, data, signer.privateKey, signer.publicKey)
        }
    }

    fun getIoyBalance(token: String, signer: WalletTemp, id: UUID, ioyType: IoyBalanceRequest.IoyType): Mono<BalanceResponse> {
        return nodeProvider.getPublicKey(token).map { node ->
            IoyBalanceRequest(
                    TokenHeader(
                            action = "br.com.blupay.token.workflows.ioy.Balance",
                            wallet = signer.id
                    ),
                    LocalWalletRequest(id),
                    TokenRequest(
                            type = tokenType,
                            issuer = node.publicKey
                    ),
                    ioyType
            )
        }.flatMap { data ->
            walletProvider.ioyBalance(token, data, signer.privateKey, signer.publicKey)
        }
    }

    fun getSettlementBalance(token: String, signer: WalletTemp, id: UUID, settlementType: SettlementBalanceRequest.SettlementType): Mono<BalanceResponse> {
        return nodeProvider.getPublicKey(token).map { node ->
            SettlementBalanceRequest(
                    TokenHeader(
                            action = "br.com.blupay.token.workflows.settlement.Balance",
                            wallet = signer.id
                    ),
                    LocalWalletRequest(id),
                    TokenRequest(
                            type = tokenType,
                            issuer = node.publicKey
                    ),
                    settlementType
            )
        }.flatMap { data ->
            walletProvider.settlementBalance(token, data, signer.privateKey, signer.publicKey)
        }
    }
}