package org.ton.lite.api.liteserver

import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.boc.BagOfCells
import org.ton.lite.api.liteserver.internal.readBoc
import org.ton.lite.api.liteserver.internal.writeBoc
import org.ton.tl.*

@Serializable
public data class LiteServerAllShardsInfo(
    val id: TonNodeBlockIdExt,
    val proof: BagOfCells,
    val data: BagOfCells
) {
    public companion object : TlCodec<LiteServerAllShardsInfo> by LiteServerAllShardsInfoTlConstructor
}

private object LiteServerAllShardsInfoTlConstructor : TlConstructor<LiteServerAllShardsInfo>(
    schema = "liteServer.allShardsInfo id:tonNode.blockIdExt proof:bytes data:bytes = liteServer.AllShardsInfo"
) {
    override fun decode(reader: TlReader): LiteServerAllShardsInfo {
        val id = reader.read(TonNodeBlockIdExt)
        val proof = reader.readBoc()
        val data = reader.readBoc()
        return LiteServerAllShardsInfo(id, proof, data)
    }

    override fun encode(writer: TlWriter, value: LiteServerAllShardsInfo) {
        writer.write(TonNodeBlockIdExt, value.id)
        writer.writeBoc(value.proof)
        writer.writeBoc(value.data)
    }
}
