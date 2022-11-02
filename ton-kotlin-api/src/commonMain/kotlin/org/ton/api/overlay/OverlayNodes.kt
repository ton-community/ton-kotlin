package org.ton.api.overlay

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.TlObject
import org.ton.tl.constructors.readVectorTl
import org.ton.tl.constructors.writeVectorTl

@Serializable
@SerialName("overlay.nodes")
class OverlayNodes(
    val nodes: List<OverlayNode>
) : TlObject<OverlayNodes>, Iterable<OverlayNode> by nodes {
    constructor(vararg nodes: OverlayNode) : this(nodes.toList())

    override fun tlCodec(): TlCodec<OverlayNodes> = Companion

    companion object : TlConstructor<OverlayNodes>(
        schema = "overlay.nodes nodes:(vector overlay.node) = overlay.Nodes",
        type = OverlayNodes::class
    ) {
        override fun encode(output: Output, value: OverlayNodes) {
            output.writeVectorTl(value.nodes, OverlayNode)
        }

        override fun decode(input: Input): OverlayNodes {
            val nodes = input.readVectorTl(OverlayNode)
            return OverlayNodes(nodes)
        }
    }
}
