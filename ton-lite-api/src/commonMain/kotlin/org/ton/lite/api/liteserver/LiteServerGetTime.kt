package org.ton.lite.api.liteserver

import io.ktor.utils.io.core.*
import org.ton.tl.TlConstructor

object LiteServerGetTime : TlConstructor<LiteServerGetTime>(
        type = LiteServerGetTime::class,
        schema = "liteServer.getTime = liteServer.CurrentTime"
) {
    override fun decode(input: Input): LiteServerGetTime = LiteServerGetTime

    override fun encode(output: Output, value: LiteServerGetTime) {
    }
}