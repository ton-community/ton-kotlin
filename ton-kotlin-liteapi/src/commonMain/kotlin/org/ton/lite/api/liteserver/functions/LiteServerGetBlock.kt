package org.ton.lite.api.liteserver.functions

import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.lite.api.liteserver.LiteServerBlockData
import org.ton.tl.*

public data class LiteServerGetBlock(
    val id: TonNodeBlockIdExt,
) : TLFunction<LiteServerGetBlock, LiteServerBlockData> {
    override fun tlCodec(): TlCodec<LiteServerGetBlock> = Companion

    override fun resultTlCodec(): TlCodec<LiteServerBlockData> = LiteServerBlockData

    public companion object : TlCodec<LiteServerGetBlock> by LiteServerGetBlockTlConstructor
}

private object LiteServerGetBlockTlConstructor : TlConstructor<LiteServerGetBlock>(
    schema = "liteServer.getBlock id:tonNode.blockIdExt = liteServer.BlockData"
) {
    override fun decode(reader: TlReader): LiteServerGetBlock {
        val id = reader.read(TonNodeBlockIdExt)
        return LiteServerGetBlock(id)
    }

    override fun encode(writer: TlWriter, value: LiteServerGetBlock) {
        writer.write(TonNodeBlockIdExt, value.id)
    }
}
