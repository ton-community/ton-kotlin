package org.ton.lite.api.liteserver

import io.ktor.utils.io.core.*
import org.ton.tl.TlConstructor

object LiteServerGetMasterchainInfo : TlConstructor<LiteServerGetMasterchainInfo>(
        type = LiteServerGetMasterchainInfo::class,
        schema = "liteServer.getMasterchainInfo = liteServer.MasterchainInfo"
) {
    override fun decode(input: Input): LiteServerGetMasterchainInfo = this

    override fun encode(output: Output, value: LiteServerGetMasterchainInfo) {
    }
}