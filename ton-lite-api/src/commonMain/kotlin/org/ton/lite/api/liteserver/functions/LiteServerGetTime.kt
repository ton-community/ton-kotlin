package org.ton.lite.api.liteserver.functions

import io.ktor.utils.io.core.*
import org.ton.lite.api.liteserver.LiteServerCurrentTime
import org.ton.tl.TlConstructor

fun interface LiteServerGetTimeFunction : LiteServerQueryFunction {
    suspend fun getTime(): LiteServerCurrentTime = query(LiteServerGetTime, LiteServerGetTime, LiteServerCurrentTime)
}

object LiteServerGetTime : TlConstructor<LiteServerGetTime>(
    type = LiteServerGetTime::class,
    schema = "liteServer.getTime = liteServer.CurrentTime"
) {
    override fun decode(input: Input): LiteServerGetTime = LiteServerGetTime

    override fun encode(output: Output, value: LiteServerGetTime) {
    }
}