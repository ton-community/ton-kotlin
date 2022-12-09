package org.ton.lite.api.liteserver.functions

import io.ktor.utils.io.core.*
import org.ton.lite.api.liteserver.LiteServerVersion
import org.ton.tl.TlConstructor

fun interface LiteServerGetVersionFunction : LiteServerQueryFunction {
    suspend fun getVersion(): LiteServerVersion =
        query(LiteServerGetVersion, LiteServerGetVersion, LiteServerVersion)
}

object LiteServerGetVersion : TlConstructor<LiteServerGetVersion>(
    schema = "liteServer.getVersion = liteServer.Version"
) {
    override fun decode(input: Input): LiteServerGetVersion = this

    override fun encode(output: Output, value: LiteServerGetVersion) {
    }
}
