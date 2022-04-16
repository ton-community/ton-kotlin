package ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SerialName("var_uint")
@Serializable
data class VarUInteger(
    val len: Int,
    val value: Long
)