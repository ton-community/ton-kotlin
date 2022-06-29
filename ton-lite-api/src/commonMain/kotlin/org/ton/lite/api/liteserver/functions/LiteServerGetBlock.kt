package org.ton.lite.api.liteserver.functions

import io.ktor.utils.io.core.*
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.lite.api.liteserver.LiteServerBlockData
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.readTl
import org.ton.tl.writeTl

fun interface LiteServerGetBlockFunction : LiteServerQueryFunction {
    suspend fun query(query: LiteServerGetBlock): LiteServerBlockData =
        query(query, LiteServerGetBlock, LiteServerBlockData)

    suspend fun getBlock(id: TonNodeBlockIdExt) = query(LiteServerGetBlock(id))
}

data class LiteServerGetBlock(
    val id: TonNodeBlockIdExt,
) {
    companion object : TlCodec<LiteServerGetBlock> by LiteServerGetBlockTlConstructor
}

private object LiteServerGetBlockTlConstructor : TlConstructor<LiteServerGetBlock>(
    type = LiteServerGetBlock::class,
    schema = "liteServer.getBlock id:tonNode.blockIdExt = liteServer.BlockData"
) {
    override fun decode(input: Input): LiteServerGetBlock {
        val id = input.readTl(TonNodeBlockIdExt)
        return LiteServerGetBlock(id)
    }

    override fun encode(output: Output, value: LiteServerGetBlock) {
        output.writeTl(TonNodeBlockIdExt, value.id)
    }
}