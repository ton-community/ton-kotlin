package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SerialName("nanocoins")
@Serializable
data class Coins(
        val amount: VarUInteger
)