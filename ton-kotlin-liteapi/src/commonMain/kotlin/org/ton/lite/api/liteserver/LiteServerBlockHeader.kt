package org.ton.lite.api.liteserver

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.boc.BagOfCells
import org.ton.lite.api.liteserver.internal.readBoc
import org.ton.lite.api.liteserver.internal.writeBoc
import org.ton.tl.*

@Serializable
public data class LiteServerBlockHeader(
    val id: TonNodeBlockIdExt,
    val mode: Int,
    @SerialName("header_proof")
    val headerProof: BagOfCells
) {
    public companion object : TlCodec<LiteServerBlockHeader> by LiteServerBlockHeaderTlConstructor
}

private object LiteServerBlockHeaderTlConstructor : TlConstructor<LiteServerBlockHeader>(
    "liteServer.blockHeader id:tonNode.blockIdExt mode:# header_proof:bytes = liteServer.BlockHeader"
) {
    override fun decode(reader: TlReader): LiteServerBlockHeader {
        val id = reader.read(TonNodeBlockIdExt)
        val mode = reader.readInt()
        val headerProof = reader.readBoc()
        return LiteServerBlockHeader(id, mode, headerProof)
    }

    override fun encode(writer: TlWriter, value: LiteServerBlockHeader) {
        writer.write(TonNodeBlockIdExt, value.id)
        writer.writeInt(value.mode)
        writer.writeBoc(value.headerProof)
    }
}
