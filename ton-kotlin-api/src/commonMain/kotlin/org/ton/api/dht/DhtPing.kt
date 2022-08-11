package org.ton.api.dht

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.TlObject
import org.ton.tl.constructors.readLongTl
import org.ton.tl.constructors.writeLongTl

@Serializable
data class DhtPing(
    val random_id: Long
) : TlObject<DhtPing> {
    override fun tlCodec(): TlCodec<DhtPing> = DhtPingTlConstructor

    companion object : TlCodec<DhtPing> by DhtPingTlConstructor
}

private object DhtPingTlConstructor : TlConstructor<DhtPing>(
    type = DhtPing::class,
    schema = "dht.ping random_id:long = dht.Pong"
) {
    override fun decode(input: Input): DhtPing {
        val random_id = input.readLongTl()
        return DhtPing(random_id)
    }

    override fun encode(output: Output, value: DhtPing) {
        output.writeLongTl(value.random_id)
    }
}