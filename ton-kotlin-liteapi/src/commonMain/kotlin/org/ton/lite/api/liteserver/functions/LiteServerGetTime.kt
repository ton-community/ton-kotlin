package org.ton.lite.api.liteserver.functions

import io.ktor.utils.io.core.*
import org.ton.lite.api.liteserver.LiteServerCurrentTime
import org.ton.tl.*

public object LiteServerGetTime : TlConstructor<LiteServerGetTime>(
    schema = "liteServer.getTime = liteServer.CurrentTime"
), TLFunction<LiteServerGetTime, LiteServerCurrentTime> {
    override fun decode(reader: TlReader): LiteServerGetTime = LiteServerGetTime
    override fun encode(writer: TlWriter, value: LiteServerGetTime) {}
    override fun tlCodec(): TlCodec<LiteServerGetTime>  = LiteServerGetTime
    override fun resultTlCodec(): TlCodec<LiteServerCurrentTime> = LiteServerCurrentTime
}
