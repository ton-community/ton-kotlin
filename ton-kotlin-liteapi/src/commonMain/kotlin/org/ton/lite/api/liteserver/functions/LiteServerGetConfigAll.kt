package org.ton.lite.api.liteserver.functions

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.lite.api.liteserver.LiteServerConfigInfo
import org.ton.tl.*

@Serializable
public data class LiteServerGetConfigAll(
    val mode: Int,
    val id: TonNodeBlockIdExt
) : TLFunction<LiteServerGetConfigAll, LiteServerConfigInfo> {
    override fun tlCodec(): TlCodec<LiteServerGetConfigAll> = Companion
    override fun resultTlCodec(): TlCodec<LiteServerConfigInfo> = LiteServerConfigInfo

    public companion object : TlCodec<LiteServerGetConfigAll> by LiteServerGetConfigAllTlConstructor
}

private object LiteServerGetConfigAllTlConstructor : TlConstructor<LiteServerGetConfigAll>(
    schema = "liteServer.getConfigAll mode:# id:tonNode.blockIdExt = liteServer.ConfigInfo"
) {
    override fun decode(reader: TlReader): LiteServerGetConfigAll {
        val mode = reader.readInt()
        val id = reader.read(TonNodeBlockIdExt)
        return LiteServerGetConfigAll(mode, id)
    }

    override fun encode(output: TlWriter, value: LiteServerGetConfigAll) {
        output.writeInt(value.mode)
        output.write(TonNodeBlockIdExt, value.id)
    }
}
