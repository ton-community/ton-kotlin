package org.ton.lite.api.liteserver.functions

import io.ktor.utils.io.core.*
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.lite.api.liteserver.LiteServerAllShardsInfo
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.readTl
import org.ton.tl.writeTl

interface LiteServerGetAllShardsInfoFunction : LiteServerQueryFunction {
    suspend fun query(query: LiteServerGetAllShardsInfo) =
        query(query, LiteServerGetAllShardsInfo, LiteServerAllShardsInfo)

    suspend fun getAllShardsInfo(id: TonNodeBlockIdExt) = query(LiteServerGetAllShardsInfo(id))
}

data class LiteServerGetAllShardsInfo(
    val id: TonNodeBlockIdExt
) {
    companion object : TlCodec<LiteServerGetAllShardsInfo> by LiteServerGetAllShardsInfoTlConstructor
}

private object LiteServerGetAllShardsInfoTlConstructor : TlConstructor<LiteServerGetAllShardsInfo>(
    schema = "liteServer.getAllShardsInfo id:tonNode.blockIdExt = liteServer.AllShardsInfo"
) {
    override fun decode(input: Input): LiteServerGetAllShardsInfo {
        val id = input.readTl(TonNodeBlockIdExt)
        return LiteServerGetAllShardsInfo(id)
    }

    override fun encode(output: Output, value: LiteServerGetAllShardsInfo) {
        output.writeTl(TonNodeBlockIdExt, value.id)
    }
}
