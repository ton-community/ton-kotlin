package ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SerialName("nanograms")
@Serializable
data class Grams(
    val amount: VarUInteger
)