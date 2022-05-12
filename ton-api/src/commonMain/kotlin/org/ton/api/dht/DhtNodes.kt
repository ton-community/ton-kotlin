package org.ton.api.dht

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.tl.TlConstructor

@Serializable
data class DhtNodes(
        val nodes: List<DhtNode>
) : Iterable<DhtNode> by nodes {
    companion object : TlConstructor<DhtNodes>(
            type = DhtNodes::class,
            schema = "dht.nodes nodes:(vector dht.node) = dht.Nodes"
    ) {
        override fun encode(output: Output, message: DhtNodes) {
            output.writeVector(message.nodes, DhtNode)
        }

        override fun decode(input: Input): DhtNodes {
            val nodes = input.readVector(DhtNode)
            return DhtNodes(nodes)
        }
    }
}