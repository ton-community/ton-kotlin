@file:UseSerializers(HexByteArraySerializer::class)

package org.ton.api.tonnode

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.ton.bitstring.Bits256
import org.ton.crypto.HexByteArraySerializer
import org.ton.tl.TlConstructor
import org.ton.tl.TlReader
import org.ton.tl.TlWriter

@Serializable
public data class TonNodeZeroStateIdExt(
    val workchain: Int,
    @SerialName("root_hash")
    val rootHash: Bits256,
    @SerialName("file_hash")
    val fileHash: Bits256
) {
    public constructor(tonNodeBlockIdExt: TonNodeBlockIdExt) : this(
        tonNodeBlockIdExt.workchain,
        tonNodeBlockIdExt.rootHash,
        tonNodeBlockIdExt.fileHash
    )

    public fun isMasterchain(): Boolean = workchain == Workchain.MASTERCHAIN_ID
    public fun isValid(): Boolean = workchain != Workchain.INVALID_WORKCHAIN

    override fun toString(): String = "($workchain:${rootHash}:${fileHash})"

    public companion object : TlConstructor<TonNodeZeroStateIdExt>(
        schema = "tonNode.zeroStateIdExt workchain:int root_hash:int256 file_hash:int256 = tonNode.ZeroStateIdExt"
    ) {
        override fun decode(reader: TlReader): TonNodeZeroStateIdExt {
            val workchain = reader.readInt()
            val rootHash = reader.readBits256()
            val fileHash = reader.readBits256()
            return TonNodeZeroStateIdExt(workchain, rootHash, fileHash)
        }

        override fun encode(writer: TlWriter, value: TonNodeZeroStateIdExt) {
            writer.writeInt(value.workchain)
            writer.writeBits256(value.rootHash)
            writer.writeBits256(value.fileHash)
        }
    }
}
