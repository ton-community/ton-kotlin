package org.ton.api.dht

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.tl.TlConstructor

@Serializable
data class DhtMessage(
        val node: DhtNode
) {
    companion object : TlConstructor<DhtMessage>(
            type = DhtMessage::class,
            schema = "dht.message node:dht.node = dht.Message"
    ) {
        override fun encode(output: Output, message: DhtMessage) {
            output.writeTl(message.node, DhtNode)
        }

        override fun decode(input: Input): DhtMessage {
            val node = input.readTl(DhtNode)
            return DhtMessage(node)
        }
    }
}