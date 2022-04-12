package ton.types.block

import kotlinx.serialization.Serializable
import ton.crypto.hex
import ton.types.util.HexByteArraySerializer

@Serializable
data class Anycast(
    val depth: Int,
    @Serializable(HexByteArraySerializer::class)
    val rewritePfx: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Anycast

        if (depth != other.depth) return false
        if (!rewritePfx.contentEquals(other.rewritePfx)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = depth
        result = 31 * result + rewritePfx.contentHashCode()
        return result
    }

    override fun toString() = "Anycast(depth=$depth, rewritePfx=${hex(rewritePfx)})"
}