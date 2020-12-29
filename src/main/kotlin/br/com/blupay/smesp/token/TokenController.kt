package br.com.blupay.smesp.token

import br.com.blupay.smesp.core.providers.token.node.NodeResponse
import br.com.blupay.smesp.core.providers.token.token.IoyMoveTokensRequest
import br.com.blupay.smesp.core.providers.token.token.IssueTokensRequest
import br.com.blupay.smesp.core.providers.token.token.MoveTokensRequest
import br.com.blupay.smesp.core.providers.token.token.RedeemTokensRequest
import br.com.blupay.smesp.core.providers.token.token.SafeMoveTokensRequest
import br.com.blupay.smesp.core.providers.token.token.SettlementMoveTokensRequest
import br.com.blupay.smesp.core.providers.token.wallet.AddAndRemoveRoleRequest
import br.com.blupay.smesp.core.providers.token.wallet.BalanceResponse
import br.com.blupay.smesp.core.providers.token.wallet.IoyBalanceRequest
import br.com.blupay.smesp.core.providers.token.wallet.IssueWallet
import br.com.blupay.smesp.core.providers.token.wallet.SettlementBalanceRequest
import br.com.blupay.smesp.core.providers.token.wallet.Wallet
import br.com.blupay.smesp.core.providers.token.wallet.WalletResponse
import br.com.blupay.smesp.core.providers.token.wallet.WalletRole
import br.com.blupay.smesp.core.services.JwsService
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.util.UUID

@RestController
@RequestMapping("token")
class TokenController(
        private val tokenNodeService: TokenNodeService,
        private val tokenWalletService: TokenWalletService,
        private val tokenService: TokenService
) {

    // TODO: remover todas as atribuições de id de carteira, elas devem ser obtidas do banco onde for necessário
    val adminWalletId = UUID.fromString("ceb5512a-3dbf-4f8b-9af9-12d94ab0d2fe") // vmotta local
//    val adminWalletId = UUID.fromString("0c4c2d03-921f-41dc-acec-e68e10254214") // Server

    val adminKeyPair = JwsService.KeyPairEncoded(
            privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC2Avgnsq/HxlzjGoMG70UUP/gQBrWypIeed6DgbVL3w1pbUXIyrJcCc23Cx+/LhbAOR9uAkzptwQ1xGOC7WkHwV8cPmQz6W1EWNkIzHSseqaauraM3ve9Ke4faJTZP1/YEUzRPzdzhUK/aXLBcC4OWM9mJHwVj15C+i1yBU0vBiXhHjZC0jf3fy3F5Niol7fiIvN0TSVxyiq0PMpPA1HIASQaT4p/63vqbagD1Bj7+45mZOKE/R5OLCrSzP79b00weyZUikpocd1yM8cCo19797serSAxoECWHM84q7gmh03VIQnheXjX0oUq/YgwZJ5n7ByOkKkegKgXJDnDWDWGlAgMBAAECggEAbBy3al0pLHEXG47T5XqafbzBjENwCfzQ0cAdPzGw5pqTQUrPmmoKCNqnh9tbBHkUamckausI+ciCQ3uTgbXLFQgOpaUzoEI9mAQCrnnU8xGygG+DU8juKj/WIi2YZTTSXR9PI1gUq4FR81K7LvFn6ABEZr6WZQfWYlhFSkw/42TRVc8u4mpoKwRyVGFrpGiHm4Kt/k/ioEQRAir7kBBnULGx+/8h0BK34ZpPNW3XTwvx3O8D9WDknWXrIQyCL0hzo3L42b4YA8dAGMrheCj7cdQwgDDHnl8AfQ8f5Sd0ShhehueGtWSME60y4KmPHfDvbezk/6UUNkTiquCxqunuwQKBgQD6eMspgrkoyva9LRK7LZEmYJ6vCkdn0PjeKmXQkNaujXb5473UhOGVB5m2QySy4XvMT7nEOW1jB+GyN4ewy4Kl+J/Vb9+sRHzgoV5tFGnopF8emVX864Bt54NcMlR7649FBxW4o6OFu8WfyZ5HOLsniVkcn57Cnx8bZjTWQ37NYwKBgQC6B11Bg/JvTE3Ub8iF+ZkIbHFzlDo99+k8zrrj1pS0mvSLSGO7DUQoT53i2DNTGyypjyU1zK+dbRmXbcebsPft752eySua0Bd/0CVo0HD9bwdHtoBENRLUKg5SRZKMBK1aCBsWc2vedvtmr7Yyd2nwZlcYt6sPZhlgjLSW0D6nVwKBgBMg2HsiFZvs7C0EXTLYRwaESHobsdpW1jq5SC9GcG7CNPzejKfHQicMfoux0wKqP7cZu5klvZnbhzwCC6XP1Plx5Fr7gTx5uoO5Lynrbi8rJmyCz1NSdOtvy9NlsJgKJFXkNZ91N87FyVDrp9/OkFRzo8+QzSIV3t1Lz+sxGmT7AoGBAKCx49jIRg/mdgTgWvOAXLVuF8KjdHw7PvsMCVzu653njP8UFTJrj0jTHavq3ssXWklOIfKLUdhHeifY6OiQwlqC9kFHvacJwoLiJq6YAEKsOjm0aLCCuDUV7zEacDELiUOQZPXL1o5hcWGOv3Bwvs6vWOU9I0M2Cw3f/c+ZK8YZAoGAJlM8BMfOFN15zG6UjwDL+N4wNJtcYCVelV73XqdezvqOGJ/NbjBc9XSQMiSD46LyrK1rkz/2YgsN9L1oV9jlKjodPgTTzO54AwC8SguYzfj/ZTYNi4EHxJX1s9J9NE/CKFRcp1/9u0pwCPy2N5LLK29+al0MOGqzFRCur+HDHvk=",
            publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtgL4J7Kvx8Zc4xqDBu9FFD/4EAa1sqSHnneg4G1S98NaW1FyMqyXAnNtwsfvy4WwDkfbgJM6bcENcRjgu1pB8FfHD5kM+ltRFjZCMx0rHqmmrq2jN73vSnuH2iU2T9f2BFM0T83c4VCv2lywXAuDljPZiR8FY9eQvotcgVNLwYl4R42QtI3938txeTYqJe34iLzdE0lccoqtDzKTwNRyAEkGk+Kf+t76m2oA9QY+/uOZmTihP0eTiwq0sz+/W9NMHsmVIpKaHHdcjPHAqNfe/e7Hq0gMaBAlhzPOKu4JodN1SEJ4Xl419KFKv2IMGSeZ+wcjpCpHoCoFyQ5w1g1hpQIDAQAB"
    )
    val signer = WalletTemp(adminWalletId, adminKeyPair.privateKey, adminKeyPair.publicKey)

    @GetMapping("/node/public-key")
    fun getPublicKey(auth: JwtAuthenticationToken): Mono<String> {
        val token = auth.token.tokenValue
        return tokenNodeService.getPublicKey(token)
    }

    @GetMapping("/node/party")
    fun getParty(
            auth: JwtAuthenticationToken,
            @RequestParam("pk") pk: String
    ): Mono<NodeResponse> {
        val token = auth.token.tokenValue
        return tokenNodeService.getParty(token, pk)
    }

    @PostMapping("/admin/load")
    fun loadAdmin(
            auth: JwtAuthenticationToken
    ): Mono<Wallet> {
        val token = auth.token.tokenValue
        return tokenWalletService.loadAdmin(token)
    }

    @GetMapping("/wallet/get")
    fun getWallet(
            auth: JwtAuthenticationToken,
            @RequestParam("id") id: UUID
    ): Mono<WalletResponse> {
        val token = auth.token.tokenValue
        return tokenWalletService.getWallet(token, signer, id)
    }

    @GetMapping("/wallet/balance")
    fun getWalletBalance(
            auth: JwtAuthenticationToken,
            @RequestParam("id") id: UUID
    ): Mono<BalanceResponse> {
        val token = auth.token.tokenValue
        return tokenWalletService.getBalance(token, signer, id)
    }

    @GetMapping("/wallet/ioy-balance")
    fun getWalletIoyBalance(
            auth: JwtAuthenticationToken,
            @RequestParam("id") id: UUID,
            @RequestParam("type") type: IoyBalanceRequest.IoyType
    ): Mono<BalanceResponse> {
        val token = auth.token.tokenValue
        return tokenWalletService.getIoyBalance(token, signer, id, type)
    }

    @GetMapping("/wallet/settlement-balance")
    fun getWalletSettlementBalance(
            auth: JwtAuthenticationToken,
            @RequestParam("id") id: UUID,
            @RequestParam("type") type: SettlementBalanceRequest.SettlementType
    ): Mono<BalanceResponse> {
        val token = auth.token.tokenValue
        return tokenWalletService.getSettlementBalance(token, signer, id, type)
    }

    @PostMapping("/wallet/issue")
    fun issueWallet(
            auth: JwtAuthenticationToken,
            @RequestBody wallet: IssueWallet
    ): Mono<Wallet> {
        val token = auth.token.tokenValue
        return tokenWalletService.issueWallet(token, wallet)
    }

    @PostMapping("/wallet/add-role")
    fun addWalletRole(
            auth: JwtAuthenticationToken,
            @RequestBody data: WalletRoleTestRequest
    ): Mono<AddAndRemoveRoleRequest> {
        val token = auth.token.tokenValue
        return tokenWalletService.addWalletRole(token, signer, data.wallet, data.role)
    }

    @PostMapping("/token/issue")
    fun issueToken(
            auth: JwtAuthenticationToken,
            @RequestBody data: IssueTokensTestRequest
    ): Mono<IssueTokensRequest> {
        val token = auth.token.tokenValue
        return tokenService.issueToken(token, signer, data.wallet, data.amount, data.maxSize)
    }

    @PutMapping("/token/move")
    fun moveToken(
            auth: JwtAuthenticationToken,
            @RequestBody data: MoveTokenTestRequest
    ): Mono<MoveTokensRequest> {
        val token = auth.token.tokenValue
        val sgr = WalletTemp(data.debtor, signer.privateKey, signer.publicKey)
        return tokenService.moveToken(token, sgr, data.debtor, data.creditors)
    }

    @PutMapping("/token/safe-move")
    fun safeMoveToken(
            auth: JwtAuthenticationToken,
            @RequestBody data: SafeMoveTokenTestRequest
    ): Mono<SafeMoveTokensRequest> {
        val token = auth.token.tokenValue
        val sgr = WalletTemp(data.debtor, signer.privateKey, signer.publicKey)
        return tokenService.safeMoveToken(token, sgr, data.dueDate, data.debtor, data.ioyWallet, data.creditors)
    }

    @PutMapping("/token/pay-ioy")
    fun payIoyToken(
            auth: JwtAuthenticationToken,
            @RequestBody data: PayIoyTestRequest
    ): Mono<IoyMoveTokensRequest> {
        val token = auth.token.tokenValue
        return tokenService.payIoyToken(token, signer, data.ioyId)
    }

    @PutMapping("/token/settlement-order")
    fun settlementOrderToken(
            auth: JwtAuthenticationToken,
            @RequestBody data: SettlementOrderTestRequest
    ): Mono<SettlementMoveTokensRequest> {
        val token = auth.token.tokenValue
        return tokenService.settlementOrderToken(token, signer, data.dueDate, data.debtor, data.cashoutWallet, data.creditors)
    }

    @PutMapping("/token/redeem")
    fun redeemToken(
            auth: JwtAuthenticationToken,
            @RequestBody data: RedeemTestRequest
    ): Mono<RedeemTokensRequest> {
        val token = auth.token.tokenValue
        return tokenService.redeemToken(token, signer, data.settlementId)
    }

    data class WalletRoleTestRequest(
            val wallet: UUID,
            val role: WalletRole
    )

    data class IssueTokensTestRequest(
            val wallet: UUID,
            val amount: Long,
            val maxSize: Long
    )

    data class MoveTokenTestRequest(
            val debtor: UUID,
            val creditors: List<Creditor>
    )

    data class SafeMoveTokenTestRequest(
            val dueDate: Long,
            val debtor: UUID,
            val ioyWallet: UUID,
            val creditors: List<Creditor>
    )

    data class PayIoyTestRequest(
            val ioyId: UUID
    )

    data class SettlementOrderTestRequest(
            val dueDate: Long,
            val debtor: UUID,
            val cashoutWallet: UUID,
            val creditors: List<Creditor>
    )

    data class RedeemTestRequest(
            val settlementId: UUID
    )
}
