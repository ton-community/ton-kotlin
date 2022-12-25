package org.ton.api.adnl

import kotlinx.serialization.Serializable
import org.ton.tl.*

@Serializable
public data class AdnlNodes(
    val nodes: Collection<AdnlNode> = emptyList()
) : Collection<AdnlNode> by nodes {
    public companion object : TlCodec<AdnlNodes> by AdnlNodesTlConstructor
}

private object AdnlNodesTlConstructor : TlConstructor<AdnlNodes>(
    schema = "adnl.nodes nodes:(vector adnl.node) = adnl.Nodes"
) {
    override fun encode(writer: TlWriter, value: AdnlNodes) = writer {
        writeCollection(value.nodes) {
            write(AdnlNode, it)
        }
    }

    override fun decode(reader: TlReader): AdnlNodes = reader {
        val nodes = readCollection {
            read(AdnlNode)
        }
        AdnlNodes(nodes)
    }
}
