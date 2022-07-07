package org.ton.lite.api.liteserver

import io.ktor.utils.io.core.*
import org.ton.tl.TlConstructor

object LiteServerQueryPrefix : TlConstructor<LiteServerQueryPrefix>(
    type = LiteServerQueryPrefix::class,
    schema = "liteServer.queryPrefix = Object"
) {
    override fun decode(input: Input): LiteServerQueryPrefix = LiteServerQueryPrefix
    override fun encode(output: Output, value: LiteServerQueryPrefix) = Unit
}