package org.ton.lite.api.liteserver

import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.boc.BagOfCells
import org.ton.lite.api.liteserver.internal.readBoc
import org.ton.lite.api.liteserver.internal.writeBoc
import org.ton.tl.*

public data class LiteServerConfigInfo(
    val mode: Int,
    val id: TonNodeBlockIdExt,
    val stateProof: BagOfCells,
    val configProof: BagOfCells
) {
    public companion object : TlCodec<LiteServerConfigInfo> by LiteServerConfigInfoTlConstructor
}

private object LiteServerConfigInfoTlConstructor : TlConstructor<LiteServerConfigInfo>(
    schema = "liteServer.configInfo mode:# id:tonNode.blockIdExt state_proof:bytes config_proof:bytes = liteServer.ConfigInfo"
) {
    override fun decode(reader: TlReader): LiteServerConfigInfo {
        val mode = reader.readInt()
        val id = reader.read(TonNodeBlockIdExt)
        val stateProof = reader.readBoc()
        val configProof = reader.readBoc()
        return LiteServerConfigInfo(mode, id, stateProof, configProof)
    }

    override fun encode(writer: TlWriter, value: LiteServerConfigInfo) {
        writer.writeInt(value.mode)
        writer.write(TonNodeBlockIdExt, value.id)
        writer.writeBoc(value.stateProof)
        writer.writeBoc(value.configProof)
    }
}
