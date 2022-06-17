package org.ton.lite.api.liteserver

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readIntTl
import org.ton.tl.constructors.writeIntTl

@Serializable
data class LiteServerCurrentTime(
        val now: Int
) {
    companion object : TlConstructor<LiteServerCurrentTime>(
            type = LiteServerCurrentTime::class,
            schema = "liteServer.currentTime now:int = liteServer.CurrentTime"
    ) {
        override fun decode(input: Input): LiteServerCurrentTime {
            val now = input.readIntTl()
            return LiteServerCurrentTime(now)
        }

        override fun encode(output: Output, value: LiteServerCurrentTime) {
            output.writeIntTl(value.now)
        }
    }
}