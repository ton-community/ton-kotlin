package org.ton.api.dht.functions

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.dht.DhtPong
import org.ton.tl.TLFunction
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readLongTl
import org.ton.tl.constructors.writeLongTl

@SerialName("dht.ping")
@Serializable
data class DhtPing(
    val random_id: Long
) : TLFunction<DhtPing, DhtPong> {
    override fun tlCodec(): TlCodec<DhtPing> = DhtPing
    override fun resultTlCodec(): TlCodec<DhtPong> = DhtPong

    companion object : TlCodec<DhtPing> by DhtPingTlConstructor
}

private object DhtPingTlConstructor : TlConstructor<DhtPing>(
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
