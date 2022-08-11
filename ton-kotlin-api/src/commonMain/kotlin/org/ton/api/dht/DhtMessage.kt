package org.ton.api.dht

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.tl.TlConstructor
import org.ton.tl.readTl
import org.ton.tl.writeTl

@Serializable
data class DhtMessage(
    val node: DhtNode
) {
    companion object : TlConstructor<DhtMessage>(
        type = DhtMessage::class,
        schema = "dht.message node:dht.node = dht.Message"
    ) {
        override fun encode(output: Output, value: DhtMessage) {
            output.writeTl(DhtNode, value.node)
        }

        override fun decode(input: Input): DhtMessage {
            val node = input.readTl(DhtNode)
            return DhtMessage(node)
        }
    }
}
