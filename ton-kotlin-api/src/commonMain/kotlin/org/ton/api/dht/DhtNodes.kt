package org.ton.api.dht

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.api.adnl.AdnlNodes
import org.ton.tl.*

@Serializable
@JsonClassDiscriminator("@type")
public data class DhtNodes(
    val nodes: Collection<DhtNode> = emptyList()
) : TlObject<DhtNodes>, Collection<DhtNode> by nodes {
    public fun toAdnlNodes(): AdnlNodes = AdnlNodes(nodes.map { it.toAdnlNode() })

    override fun tlCodec(): TlCodec<DhtNodes> = Companion

    public companion object : TlConstructor<DhtNodes>(
        schema = "dht.nodes nodes:(vector dht.node) = dht.Nodes"
    ) {
        override fun encode(writer: TlWriter, value: DhtNodes) {
            writer.writeCollection(value.nodes) {
                write(DhtNode, it)
            }
        }

        override fun decode(reader: TlReader): DhtNodes {
            val nodes = reader.readCollection {
                read(DhtNode)
            }
            return DhtNodes(nodes)
        }
    }
}
