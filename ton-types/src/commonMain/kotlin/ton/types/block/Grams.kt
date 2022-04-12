package ton.types.block

import kotlinx.serialization.Serializable

@Serializable
data class Grams(
    val amount: VarUInteger
)