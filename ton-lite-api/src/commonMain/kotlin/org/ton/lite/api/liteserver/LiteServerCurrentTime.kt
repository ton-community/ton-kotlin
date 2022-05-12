package org.ton.lite.api.liteserver

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.tl.TlConstructor

@Serializable
data class LiteServerCurrentTime(
        val now: Int
) {
    companion object : TlConstructor<LiteServerCurrentTime>(
            type = LiteServerCurrentTime::class,
            schema = "liteServer.currentTime now:int = liteServer.CurrentTime"
    ) {
        override fun decode(input: Input): LiteServerCurrentTime {
            val now = input.readIntLittleEndian()
            return LiteServerCurrentTime(now)
        }

        override fun encode(output: Output, message: LiteServerCurrentTime) {
            output.writeIntLittleEndian(message.now)
        }
    }
}