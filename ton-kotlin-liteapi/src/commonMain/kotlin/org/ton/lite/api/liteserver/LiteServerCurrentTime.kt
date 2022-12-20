package org.ton.lite.api.liteserver

import kotlinx.serialization.Serializable
import org.ton.tl.*

@Serializable
public data class LiteServerCurrentTime(
    val now: Int
) {
    public companion object : TlConstructor<LiteServerCurrentTime>(
        schema = "liteServer.currentTime now:int = liteServer.CurrentTime"
    ) {
        override fun decode(reader: TlReader): LiteServerCurrentTime {
            val now = reader.readInt()
            return LiteServerCurrentTime(now)
        }

        override fun encode(writer: TlWriter, value: LiteServerCurrentTime) {
            writer.writeInt(value.now)
        }
    }
}
