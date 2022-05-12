@file:UseSerializers(HexByteArraySerializer::class)

package org.ton.lite.api.liteserver

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.ton.crypto.Base64ByteArraySerializer
import org.ton.crypto.HexByteArraySerializer
import org.ton.crypto.base64
import org.ton.tl.TlConstructor

@Serializable
data class LiteServerAccountId(
        val workchain: Int,
        @Serializable(Base64ByteArraySerializer::class)
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

    override fun toString(): String = buildString {
        append("LiteServerAccountId(workchain=")
        append(workchain)
        append(", id=")
        append(base64(id))
        append(")")
    }

    companion object : TlConstructor<LiteServerAccountId>(
            type = LiteServerAccountId::class,
            schema = "liteServer.accountId workchain:int id:int256 = liteServer.AccountId"
    ) {
        override fun decode(input: Input): LiteServerAccountId {
            val workchain = input.readIntLittleEndian()
            val id = input.readBits256()
            return LiteServerAccountId(workchain, id)
        }

        override fun encode(output: Output, message: LiteServerAccountId) {
            output.writeIntLittleEndian(message.workchain)
            output.writeBits256(message.id)
        }
    }
}