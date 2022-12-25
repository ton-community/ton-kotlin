package org.ton.lite.api.liteserver.functions

import org.ton.lite.api.liteserver.LiteServerMasterchainInfo
import org.ton.tl.*

public object LiteServerGetMasterchainInfo :
    TLFunction<LiteServerGetMasterchainInfo, LiteServerMasterchainInfo>,
    TlConstructor<LiteServerGetMasterchainInfo>(
        schema = "liteServer.getMasterchainInfo = liteServer.MasterchainInfo"
    ) {
    override fun tlCodec(): TlCodec<LiteServerGetMasterchainInfo> = this
    override fun resultTlCodec(): TlCodec<LiteServerMasterchainInfo> = LiteServerMasterchainInfo

    override fun decode(reader: TlReader): LiteServerGetMasterchainInfo = this
    override fun encode(writer: TlWriter, value: LiteServerGetMasterchainInfo) {
    }
}
