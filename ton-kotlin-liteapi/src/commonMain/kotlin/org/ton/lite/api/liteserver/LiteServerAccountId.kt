@file:UseSerializers(HexByteArraySerializer::class)

package org.ton.lite.api.liteserver

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.ton.crypto.HexByteArraySerializer
import org.ton.tl.TlConstructor
import org.ton.tl.TlReader
import org.ton.tl.TlWriter

@Serializable
@SerialName("liteServer.accountId")
public data class LiteServerAccountId(
    val workchain: Int,
    val id: ByteArray
) {
    init {
        require(id.size == 32) { "id must be 32 bytes long" }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LiteServerAccountId) return false

        if (workchain != other.workchain) return false
        if (!id.contentEquals(other.id)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = workchain
        result = 31 * result + id.contentHashCode()
        return result
    }

    public companion object : TlConstructor<LiteServerAccountId>(
        schema = "liteServer.accountId workchain:int id:int256 = liteServer.AccountId"
    ) {
        override fun decode(reader: TlReader): LiteServerAccountId {
            val workchain = reader.readInt()
            val id = reader.readRaw(32)
            return LiteServerAccountId(workchain, id)
        }

        override fun encode(writer: TlWriter, value: LiteServerAccountId) {
            writer.writeInt(value.workchain)
            writer.writeRaw(value.id)
        }
    }
}
