@file:UseSerializers(HexByteArraySerializer::class)

package org.ton.lite.api.liteserver

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.ton.api.tonnode.Workchain
import org.ton.bitstring.BitString
import org.ton.bitstring.toBitString
import org.ton.block.AddrStd
import org.ton.crypto.HexByteArraySerializer
import org.ton.tl.Bits256
import org.ton.tl.TlConstructor
import org.ton.tl.TlReader
import org.ton.tl.TlWriter

@Serializable
public data class LiteServerAccountId(
    val workchain: Int,
    val id: Bits256
) {
    public constructor() : this(Workchain.INVALID_WORKCHAIN, Bits256())
    public constructor(workchain: Int, id: ByteArray) : this(workchain, Bits256(id))
    public constructor(string: String) : this(AddrStd(string))
    public constructor(addrStd: AddrStd) : this(addrStd.workchain_id, Bits256(addrStd.address))

    public fun toMsgAddressIntStd(): AddrStd = AddrStd(workchain, id.toBitString())

    override fun toString(): String = "$workchain:$id"

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
