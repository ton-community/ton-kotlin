package org.ton.lite.api.liteserver.functions

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.lite.api.liteserver.LiteServerConfigInfo
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readIntTl
import org.ton.tl.constructors.writeIntTl
import org.ton.tl.readTl
import org.ton.tl.writeTl

interface LiteServerGetConfigAllFunction : LiteServerQueryFunction {
    suspend fun query(query: LiteServerGetConfigAll) =
        query(query, LiteServerGetConfigAll, LiteServerConfigInfo)

    suspend fun getConfigAll(
        mode: Int,
        id: TonNodeBlockIdExt
    ) = query(LiteServerGetConfigAll(mode, id))
}

@Serializable
data class LiteServerGetConfigAll(
    val mode: Int,
    val id: TonNodeBlockIdExt
) {
    companion object : TlCodec<LiteServerGetConfigAll> by LiteServerGetConfigAllTlConstructor
}

private object LiteServerGetConfigAllTlConstructor : TlConstructor<LiteServerGetConfigAll>(
    schema = "liteServer.getConfigAll mode:# id:tonNode.blockIdExt = liteServer.ConfigInfo"
) {
    override fun decode(input: Input): LiteServerGetConfigAll {
        val mode = input.readIntTl()
        val id = input.readTl(TonNodeBlockIdExt)
        return LiteServerGetConfigAll(mode, id)
    }

    override fun encode(output: Output, value: LiteServerGetConfigAll) {
        output.writeIntTl(value.mode)
        output.writeTl(TonNodeBlockIdExt, value.id)
    }
}
