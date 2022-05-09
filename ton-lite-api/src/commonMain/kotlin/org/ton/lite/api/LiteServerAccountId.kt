@file:UseSerializers(HexByteArraySerializer::class)

package org.ton.lite.api

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.ton.crypto.HexByteArraySerializer
import org.ton.crypto.hex
import org.ton.tl.TLCodec

@Serializable
data class LiteServerAccountId(
        val workchain: Int,
        val id: ByteArray
) {
    init {
        check(id.size == 32)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LiteServerAccountId

        if (workchain != other.workchain) return false
        if (!id.contentEquals(other.id)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = workchain
        result = 31 * result + id.contentHashCode()
        return result
    }

    override fun toString(): String = "LiteServerAccountId(workchain=$workchain, id=${hex(id)})"

    companion object : TLCodec<LiteServerAccountId> {
        override val id: Int = 1973478085

        override fun decode(input: Input): LiteServerAccountId {
            val workchain = input.readIntLittleEndian()
            val id = input.readBytes(32)
            return LiteServerAccountId(workchain, id)
        }

        override fun encode(output: Output, message: LiteServerAccountId) {
            output.writeIntLittleEndian(message.workchain)
            output.writeFully(message.id)
        }
    }
}