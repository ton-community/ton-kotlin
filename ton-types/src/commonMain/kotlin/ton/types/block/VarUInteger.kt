package ton.types.block

import kotlinx.serialization.Serializable

@Serializable
data class VarUInteger(
    val len: Int,
    val value: Long
)