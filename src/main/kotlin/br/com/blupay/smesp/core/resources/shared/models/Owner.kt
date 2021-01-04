package br.com.blupay.smesp.core.resources.shared.models

import br.com.blupay.smesp.core.resources.shared.enums.UserTypes
import java.util.UUID

data class Owner(val id: UUID,
                 val register: String,
                 val type: UserTypes
)
