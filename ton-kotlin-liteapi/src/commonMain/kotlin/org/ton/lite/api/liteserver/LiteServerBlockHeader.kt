package org.ton.lite.api.liteserver

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.tl.*

@Serializable
@SerialName("liteServer.blockHeader")
public data class LiteServerBlockHeader(
    val id: TonNodeBlockIdExt,
    val mode: Int,
    @SerialName("header_proof")
    val headerProof: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LiteServerBlockHeader) return false

        if (id != other.id) return false
        if (mode != other.mode) return false
        if (!headerProof.contentEquals(other.headerProof)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + mode
        result = 31 * result + headerProof.contentHashCode()
        return result
    }

    public companion object : TlCodec<LiteServerBlockHeader> by LiteServerBlockHeaderTlConstructor
}

private object LiteServerBlockHeaderTlConstructor : TlConstructor<LiteServerBlockHeader>(
    "liteServer.blockHeader id:tonNode.blockIdExt mode:# header_proof:bytes = liteServer.BlockHeader"
) {
    override fun decode(reader: TlReader): LiteServerBlockHeader {
        val id = reader.read(TonNodeBlockIdExt)
        val mode = reader.readInt()
        val headerProof = reader.readBytes()
        return LiteServerBlockHeader(id, mode, headerProof)
    }

    override fun encode(writer: TlWriter, value: LiteServerBlockHeader) {
        writer.write(TonNodeBlockIdExt, value.id)
        writer.writeInt(value.mode)
        writer.writeBytes(value.headerProof)
    }
}
