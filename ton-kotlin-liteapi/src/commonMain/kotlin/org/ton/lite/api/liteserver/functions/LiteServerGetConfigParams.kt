package org.ton.lite.api.liteserver.functions

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.lite.api.liteserver.LiteServerConfigInfo
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.*
import org.ton.tl.readTl
import org.ton.tl.writeTl

interface LiteServerGetConfigParamsFunction : LiteServerQueryFunction {
    suspend fun query(query: LiteServerGetConfigParams) =
        query(query, LiteServerGetConfigParams, LiteServerConfigInfo)

    suspend fun getConfigParams(
        mode: Int,
        id: TonNodeBlockIdExt,
        param_list: List<Int>
    ) = query(LiteServerGetConfigParams(mode, id, param_list))
}

@Serializable
data class LiteServerGetConfigParams(
    val mode: Int,
    val id: TonNodeBlockIdExt,
    val param_list: List<Int>
) {
    companion object : TlCodec<LiteServerGetConfigParams> by LiteServerGetConfigParamsTlConstructor
}

private object LiteServerGetConfigParamsTlConstructor : TlConstructor<LiteServerGetConfigParams>(
    schema = "liteServer.getConfigParams mode:# id:tonNode.blockIdExt param_list:(vector int) = liteServer.ConfigInfo"
) {
    override fun decode(input: Input): LiteServerGetConfigParams {
        val mode = input.readIntTl()
        val id = input.readTl(TonNodeBlockIdExt)
        val paramList = input.readVectorTl(IntTlConstructor)
        return LiteServerGetConfigParams(mode, id, paramList)
    }

    override fun encode(output: Output, value: LiteServerGetConfigParams) {
        output.writeIntTl(value.mode)
        output.writeTl(TonNodeBlockIdExt, value.id)
        output.writeVectorTl(value.param_list, IntTlConstructor)
    }
}
