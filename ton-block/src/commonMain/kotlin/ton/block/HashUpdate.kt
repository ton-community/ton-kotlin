@file:UseSerializers(HexByteArraySerializer::class)

package ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import ton.crypto.HexByteArraySerializer
import ton.crypto.hex

@SerialName("update_hashes")
@Serializable
data class HashUpdate<T>(
    val old_hash: ByteArray,
    val new_hash: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HashUpdate<*>

        if (!old_hash.contentEquals(other.old_hash)) return false
        if (!new_hash.contentEquals(other.new_hash)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = old_hash.contentHashCode()
        result = 31 * result + new_hash.contentHashCode()
        return result
    }

    override fun toString(): String =
        "HashUpdate(old_hash=${hex(old_hash)}, new_hash=${hex(new_hash)})"
}