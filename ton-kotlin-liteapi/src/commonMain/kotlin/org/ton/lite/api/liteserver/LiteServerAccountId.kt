@file:UseSerializers(HexByteArraySerializer::class)

package org.ton.lite.api.liteserver

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.ton.bitstring.Bits256
import org.ton.crypto.HexByteArraySerializer
import org.ton.tl.TlConstructor
import org.ton.tl.TlReader
import org.ton.tl.TlWriter

@Serializable
@SerialName("liteServer.accountId")
public data class LiteServerAccountId(
    val workchain: Int,
    val id: Bits256
) {
    public companion object : TlConstructor<LiteServerAccountId>(
        schema = "liteServer.accountId workchain:int id:int256 = liteServer.AccountId"
    ) {
        override fun decode(reader: TlReader): LiteServerAccountId {
            val workchain = reader.readInt()
            val id = reader.readBits256()
            return LiteServerAccountId(workchain, id)
        }

        override fun encode(writer: TlWriter, value: LiteServerAccountId) {
            writer.writeInt(value.workchain)
            writer.writeBits256(value.id)
        }
    }
}
