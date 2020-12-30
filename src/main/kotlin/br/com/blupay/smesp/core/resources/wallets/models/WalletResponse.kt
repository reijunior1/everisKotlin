package br.com.blupay.smesp.core.resources.wallets.models

import br.com.blupay.smesp.core.resources.shared.enums.UserTypes
import java.util.UUID

data class WalletResponse(
    val id: UUID,
    val owner: UUID,
    val token: UUID,
    val type: UserTypes
)