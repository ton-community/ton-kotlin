package org.ton.lite.api.liteserver

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readIntTl
import org.ton.tl.constructors.readLongTl
import org.ton.tl.constructors.writeIntTl
import org.ton.tl.constructors.writeLongTl

@Serializable
data class LiteServerVersion(
    val mode: Int,
    val version: Int,
    val capabilities: Long,
    val now: Int
) {
    companion object : TlConstructor<LiteServerVersion>(
        type = LiteServerVersion::class,
        schema = "liteServer.version mode:# version:int capabilities:long now:int = liteServer.Version"
    ) {
        override fun decode(input: Input): LiteServerVersion {
            val mode = input.readIntTl()
            val version = input.readIntTl()
            val capabilities = input.readLongTl()
            val now = input.readIntTl()
            return LiteServerVersion(mode, version, capabilities, now)
        }

        override fun encode(output: Output, value: LiteServerVersion) {
            output.writeIntTl(value.mode)
            output.writeIntTl(value.version)
            output.writeLongTl(value.capabilities)
            output.writeIntTl(value.now)
        }
    }
}