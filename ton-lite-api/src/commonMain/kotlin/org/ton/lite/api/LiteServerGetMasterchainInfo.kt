package org.ton.lite.api

import io.ktor.utils.io.core.*
import org.ton.tl.TLCodec

object LiteServerGetMasterchainInfo : TLCodec<LiteServerGetMasterchainInfo> {
    override val id: Int = -1984567762

    override fun decode(input: Input): LiteServerGetMasterchainInfo = this

    override fun encode(output: Output, message: LiteServerGetMasterchainInfo) {
    }
}