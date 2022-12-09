package org.ton.api.dht

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.tl.*

@Serializable
data class DhtMessage(
    val node: DhtNode
) : TlObject<DhtMessage> {
    override fun tlCodec(): TlCodec<DhtMessage> = Companion

    companion object : TlConstructor<DhtMessage>(
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
