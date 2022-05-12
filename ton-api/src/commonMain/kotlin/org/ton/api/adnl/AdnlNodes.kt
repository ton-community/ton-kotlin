package org.ton.api.adnl

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.tl.TlConstructor

@Serializable
data class AdnlNodes(
        val nodes: List<AdnlNode>
) : Iterable<AdnlNode> by nodes {
    companion object : TlConstructor<AdnlNodes>(
            type = AdnlNodes::class,
            schema = "adnl.nodes nodes:(vector adnl.node) = adnl.Nodes"
    ) {
        override fun encode(output: Output, message: AdnlNodes) {
            output.writeVector(message.nodes, AdnlNode)
        }

        override fun decode(input: Input): AdnlNodes {
            val nodes = input.readVector(AdnlNode)
            return AdnlNodes(nodes)
        }
    }
}