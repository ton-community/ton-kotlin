package org.ton.lite.api.liteserver

import io.ktor.utils.io.core.*
import org.ton.tl.TlConstructor

object LiteServerQueryPrefix : TlConstructor<LiteServerQueryPrefix>(
    schema = "liteServer.queryPrefix = Object"
) {
    override fun decode(input: Input): LiteServerQueryPrefix = LiteServerQueryPrefix
    override fun encode(output: Output, value: LiteServerQueryPrefix) = Unit
}
