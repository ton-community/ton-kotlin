package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.crypto.HexByteArraySerializer
import org.ton.crypto.hex

@SerialName("anycast_info")
@Serializable
data class Anycast(
    val depth: Int,
    @Serializable(HexByteArraySerializer::class)
    val rewrite_pfx: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Anycast

        if (depth != other.depth) return false
        if (!rewrite_pfx.contentEquals(other.rewrite_pfx)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = depth
        result = 31 * result + rewrite_pfx.contentHashCode()
        return result
    }

    override fun toString() = buildString {
        append("Anycast(depth=")
        append(depth)
        append(", rewrite_pfx=")
        append(hex(rewrite_pfx))
        append(")")
    }
}