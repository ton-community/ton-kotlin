package org.ton.api.dht

import io.ktor.utils.io.core.*
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.TlObject

object DhtStored : TlConstructor<DhtStored>(
    schema = "dht.stored = dht.Stored"
), TlObject<DhtStored> {
    override fun tlCodec(): TlCodec<DhtStored> = this

    override fun encode(output: Output, value: DhtStored) {
    }

    override fun decode(input: Input): DhtStored = DhtStored
}
