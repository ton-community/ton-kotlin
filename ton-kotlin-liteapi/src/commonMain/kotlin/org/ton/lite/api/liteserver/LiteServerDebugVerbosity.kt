package org.ton.lite.api.liteserver

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readIntTl
import org.ton.tl.constructors.writeIntTl

@Serializable
data class LiteServerDebugVerbosity(
    val value: Int
) {
    companion object : TlCodec<LiteServerDebugVerbosity> by LiteServerDebugVerbosityTlConstructor
}

private object LiteServerDebugVerbosityTlConstructor : TlConstructor<LiteServerDebugVerbosity>(
    schema = "liteServer.debug.verbosity value:int = liteServer.debug.Verbosity"
) {
    override fun decode(input: Input): LiteServerDebugVerbosity {
        val value = input.readIntTl()
        return LiteServerDebugVerbosity(value)
    }

    override fun encode(output: Output, value: LiteServerDebugVerbosity) {
        output.writeIntTl(value.value)
    }
}
