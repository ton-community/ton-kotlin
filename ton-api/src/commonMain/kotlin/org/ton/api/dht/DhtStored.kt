package org.ton.api.dht

import io.ktor.utils.io.core.*
import org.ton.tl.TlConstructor

object DhtStored : TlConstructor<DhtStored>(
        type = DhtStored::class,
        schema = "dht.stored = dht.Stored"
) {
    override fun encode(output: Output, message: DhtStored) {
    }

    override fun decode(input: Input): DhtStored = DhtStored
}