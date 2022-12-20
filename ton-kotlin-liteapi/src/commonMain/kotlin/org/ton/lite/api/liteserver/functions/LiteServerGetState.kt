package org.ton.lite.api.liteserver.functions

import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.lite.api.liteserver.LiteServerBlockState
import org.ton.tl.*

@Serializable
public data class LiteServerGetState(
    val id: TonNodeBlockIdExt,
) : TLFunction<LiteServerGetState, LiteServerBlockState> {
    public companion object : TlCodec<LiteServerGetState> by LiteServerGetStateTlConstructor

    override fun tlCodec(): TlCodec<LiteServerGetState> = LiteServerGetState

    override fun resultTlCodec(): TlCodec<LiteServerBlockState>  = LiteServerBlockState
}

private object LiteServerGetStateTlConstructor : TlConstructor<LiteServerGetState>(
    schema = "liteServer.getState id:tonNode.blockIdExt = liteServer.BlockState"
) {
    override fun decode(reader: TlReader): LiteServerGetState {
        val id = reader.read(TonNodeBlockIdExt)
        return LiteServerGetState(id)
    }

    override fun encode(writer: TlWriter, value: LiteServerGetState) {
        writer.write(TonNodeBlockIdExt, value.id)
    }
}
