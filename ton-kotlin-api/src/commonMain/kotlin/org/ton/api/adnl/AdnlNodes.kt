package org.ton.api.adnl

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readVectorTl
import org.ton.tl.constructors.writeVectorTl

@Serializable
data class AdnlNodes(
    val nodes: List<AdnlNode> = emptyList()
) : Iterable<AdnlNode> by nodes {
    companion object : TlConstructor<AdnlNodes>(
        type = AdnlNodes::class,
        schema = "adnl.nodes nodes:(vector adnl.node) = adnl.Nodes"
    ) {
        override fun encode(output: Output, value: AdnlNodes) {
            output.writeVectorTl(value.nodes, AdnlNode)
        }

        override fun decode(input: Input): AdnlNodes {
            val nodes = input.readVectorTl(AdnlNode)
            return AdnlNodes(nodes)
        }
    }
}
