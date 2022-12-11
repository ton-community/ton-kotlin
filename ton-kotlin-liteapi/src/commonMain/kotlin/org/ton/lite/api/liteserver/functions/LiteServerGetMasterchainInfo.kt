package org.ton.lite.api.liteserver.functions

import io.ktor.utils.io.core.*
import org.ton.lite.api.liteserver.LiteServerMasterchainInfo
import org.ton.tl.TLFunction
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor

fun interface LiteServerGetMasterchainInfoFunction : LiteServerQueryFunction {
    suspend fun getMasterchainInfo(seqno: Int = -1): LiteServerMasterchainInfo =
        query(LiteServerGetMasterchainInfo, LiteServerGetMasterchainInfo, LiteServerMasterchainInfo, seqno)
}

object LiteServerGetMasterchainInfo :
    TLFunction<LiteServerGetMasterchainInfo, LiteServerMasterchainInfo>,
    TlConstructor<LiteServerGetMasterchainInfo>(
        schema = "liteServer.getMasterchainInfo = liteServer.MasterchainInfo"
    ) {
    override fun tlCodec(): TlCodec<LiteServerGetMasterchainInfo> = this
    override fun resultTlCodec(): TlCodec<LiteServerMasterchainInfo> = LiteServerMasterchainInfo

    override fun decode(input: Input): LiteServerGetMasterchainInfo = this

    override fun encode(output: Output, value: LiteServerGetMasterchainInfo) {
    }
}
