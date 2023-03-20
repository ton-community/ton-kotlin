@file:UseSerializers(HexByteArraySerializer::class)

package org.ton.lite.api.liteserver

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.ton.bitstring.BitString
import org.ton.crypto.HexByteArraySerializer
import org.ton.tl.ByteString
import org.ton.tl.ByteString.Companion.toByteString
import org.ton.tl.TlConstructor
import org.ton.tl.TlReader
import org.ton.tl.TlWriter
import kotlin.jvm.JvmName

@Serializable
@SerialName("liteServer.accountId")
public data class LiteServerAccountId(
    @get:JvmName("workchain")
    val workchain: Int,
    @get:JvmName("id")
    val id: ByteString
) {
    public constructor(workchain: Int, id: ByteArray) : this(workchain, id.toByteString())
    public constructor(workchain: Int, id: BitString) : this(workchain, id.toByteArray())

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
            writer.writeRaw(value.id.toByteArray())
        }
    }
}
