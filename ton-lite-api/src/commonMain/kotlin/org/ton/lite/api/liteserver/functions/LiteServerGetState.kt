package org.ton.lite.api.liteserver.functions

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.lite.api.liteserver.LiteServerBlockState
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.readTl
import org.ton.tl.writeTl

fun interface LiteServerGetStateFunction : LiteServerQueryFunction {
    suspend fun query(query: LiteServerGetState): LiteServerBlockState = query(
        query, LiteServerGetState, LiteServerBlockState
    )

    suspend fun getState(id: TonNodeBlockIdExt) = query(LiteServerGetState(id))
}

@Serializable
data class LiteServerGetState(
    val id: TonNodeBlockIdExt,
) {
    companion object : TlCodec<LiteServerGetState> by LiteServerGetStateTlConstructor
}

private object LiteServerGetStateTlConstructor : TlConstructor<LiteServerGetState>(
    type = LiteServerGetState::class,
    schema = "liteServer.getState id:tonNode.blockIdExt = liteServer.BlockState"
) {
    override fun decode(input: Input): LiteServerGetState {
        val id = input.readTl(TonNodeBlockIdExt)
        return LiteServerGetState(id)
    }

    override fun encode(output: Output, value: LiteServerGetState) {
        output.writeTl(TonNodeBlockIdExt, value.id)
    }
}