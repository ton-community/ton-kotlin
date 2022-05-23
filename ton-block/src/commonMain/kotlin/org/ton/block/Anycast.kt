package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString

@SerialName("anycast_info")
@Serializable
data class Anycast(
    val depth: Int,
    @SerialName("rewrite_pfx")
    val rewritePfx: BitString
) {
    init {
        require(depth >= 1) { "depth >= 1" }
    }
}
