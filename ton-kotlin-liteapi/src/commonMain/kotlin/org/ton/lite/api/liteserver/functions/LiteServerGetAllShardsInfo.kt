package org.ton.lite.api.liteserver.functions

import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.lite.api.liteserver.LiteServerAllShardsInfo
import org.ton.tl.*

public data class LiteServerGetAllShardsInfo(
    val id: TonNodeBlockIdExt
) : TLFunction<LiteServerGetAllShardsInfo, LiteServerAllShardsInfo> {
    override fun tlCodec(): TlCodec<LiteServerGetAllShardsInfo> = Companion
    override fun resultTlCodec(): TlCodec<LiteServerAllShardsInfo> = LiteServerAllShardsInfo

    public companion object : TlCodec<LiteServerGetAllShardsInfo> by LiteServerGetAllShardsInfoTlConstructor
}

private object LiteServerGetAllShardsInfoTlConstructor : TlConstructor<LiteServerGetAllShardsInfo>(
    schema = "liteServer.getAllShardsInfo id:tonNode.blockIdExt = liteServer.AllShardsInfo"
) {
    override fun decode(reader: TlReader): LiteServerGetAllShardsInfo {
        val id = reader.read(TonNodeBlockIdExt)
        return LiteServerGetAllShardsInfo(id)
    }

    override fun encode(writer: TlWriter, value: LiteServerGetAllShardsInfo) {
        writer.write(TonNodeBlockIdExt, value.id)
    }
}
