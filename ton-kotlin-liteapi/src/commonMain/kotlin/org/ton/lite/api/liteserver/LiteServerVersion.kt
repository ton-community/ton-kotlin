package org.ton.lite.api.liteserver

import kotlinx.serialization.Serializable
import org.ton.tl.TlConstructor
import org.ton.tl.TlReader
import org.ton.tl.TlWriter

@Serializable
public data class LiteServerVersion(
    val mode: Int,
    val version: Int,
    val capabilities: Long,
    val now: Int
) {
    public companion object : TlConstructor<LiteServerVersion>(
        schema = "liteServer.version mode:# version:int capabilities:long now:int = liteServer.Version"
    ) {
        override fun decode(reader: TlReader): LiteServerVersion {
            val mode = reader.readInt()
            val version = reader.readInt()
            val capabilities = reader.readLong()
            val now = reader.readInt()
            return LiteServerVersion(mode, version, capabilities, now)
        }

        override fun encode(writer: TlWriter, value: LiteServerVersion) {
            writer.writeInt(value.mode)
            writer.writeInt(value.version)
            writer.writeLong(value.capabilities)
            writer.writeInt(value.now)
        }
    }
}
