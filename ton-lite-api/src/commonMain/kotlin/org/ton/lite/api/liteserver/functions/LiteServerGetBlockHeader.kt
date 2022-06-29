package org.ton.lite.api.liteserver.functions

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.lite.api.liteserver.LiteServerBlockHeader
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readIntTl
import org.ton.tl.constructors.writeIntTl
import org.ton.tl.readTl
import org.ton.tl.writeTl

fun interface LiteServerGetBlockHeaderFunction : LiteServerQueryFunction {
    suspend fun query(query: LiteServerGetBlockHeader) =
        query(query, LiteServerGetBlockHeader, LiteServerBlockHeader)

    suspend fun getBlockHeader(id: TonNodeBlockIdExt, mode: Int) = query(LiteServerGetBlockHeader(id, mode))
}

@Serializable
data class LiteServerGetBlockHeader(
    val id: TonNodeBlockIdExt,
    val mode: Int
) {
    companion object : TlCodec<LiteServerGetBlockHeader> by LiteServerGetBlockHeaderTlConstructor
}

private object LiteServerGetBlockHeaderTlConstructor : TlConstructor<LiteServerGetBlockHeader>(
    type = LiteServerGetBlockHeader::class,
    schema = "liteServer.getBlockHeader id:tonNode.blockIdExt mode:# = liteServer.BlockHeader"
) {
    override fun decode(input: Input): LiteServerGetBlockHeader {
        val id = input.readTl(TonNodeBlockIdExt)
        val mode = input.readIntTl()
        return LiteServerGetBlockHeader(id, mode)
    }

    override fun encode(output: Output, value: LiteServerGetBlockHeader) {
        output.writeTl(TonNodeBlockIdExt, value.id)
        output.writeIntTl(value.mode)
    }
}