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