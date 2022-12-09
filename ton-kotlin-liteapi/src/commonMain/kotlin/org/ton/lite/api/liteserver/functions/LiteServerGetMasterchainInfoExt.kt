package org.ton.lite.api.liteserver.functions

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.lite.api.liteserver.LiteServerMasterchainInfoExt
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readIntTl
import org.ton.tl.constructors.writeIntTl

fun interface LiteServerGetMasterchainInfoExtFunction : LiteServerQueryFunction {
    suspend fun query(query: LiteServerGetMasterchainInfoExt) =
        query(query, LiteServerGetMasterchainInfoExt, LiteServerMasterchainInfoExt)

    suspend fun getMasterchainInfoExt(mode: Int) = query(LiteServerGetMasterchainInfoExt(mode))
}

@Serializable
data class LiteServerGetMasterchainInfoExt(
    val mode: Int
) {
    companion object : TlCodec<LiteServerGetMasterchainInfoExt> by LiteServerGetMasterchainInfoExtTlConstructor
}

private object LiteServerGetMasterchainInfoExtTlConstructor : TlConstructor<LiteServerGetMasterchainInfoExt>(
    schema = "liteServer.getMasterchainInfoExt mode:# = liteServer.MasterchainInfoExt"
) {
    override fun decode(input: Input): LiteServerGetMasterchainInfoExt {
        val mode = input.readIntTl()
        return LiteServerGetMasterchainInfoExt(mode)
    }

    override fun encode(output: Output, value: LiteServerGetMasterchainInfoExt) {
        output.writeIntTl(value.mode)
    }
}
