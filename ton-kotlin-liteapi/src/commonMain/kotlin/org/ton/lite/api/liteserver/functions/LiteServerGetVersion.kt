package org.ton.lite.api.liteserver.functions

import io.ktor.utils.io.core.*
import org.ton.lite.api.liteserver.LiteServerVersion
import org.ton.tl.*


public object LiteServerGetVersion : TlConstructor<LiteServerGetVersion>(
    schema = "liteServer.getVersion = liteServer.Version"
), TLFunction<LiteServerGetVersion, LiteServerVersion> {
    override fun decode(reader: TlReader): LiteServerGetVersion  = this

    override fun encode(writer: TlWriter, value: LiteServerGetVersion) {}

    override fun tlCodec(): TlCodec<LiteServerGetVersion> = LiteServerGetVersion

    override fun resultTlCodec(): TlCodec<LiteServerVersion> = LiteServerVersion
}
