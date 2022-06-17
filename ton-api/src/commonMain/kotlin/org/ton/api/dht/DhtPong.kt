package org.ton.api.dht

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.tl.TlConstructor

@Serializable
data class DhtPong(
        @SerialName("random_id")
        val randomId: Long
) {
    companion object : TlConstructor<DhtPong>(
            type = DhtPong::class,
            schema = "dht.pong random_id:long = dht.Pong"
    ) {
        override fun encode(output: Output, value: DhtPong) {
            output.writeLongLittleEndian(value.randomId)
        }

        override fun decode(input: Input): DhtPong {
            val randomId = input.readLongLittleEndian()
            return DhtPong(randomId)
        }
    }
}