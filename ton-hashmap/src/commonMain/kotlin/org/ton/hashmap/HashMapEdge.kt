package org.ton.hashmap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString

@Serializable
@SerialName("hm_edge")
data class HashMapEdge<T : Any>(
    val label: HashMapLabel,
    val node: HashMapNode<T>
) {
    override fun toString(): String = "hm_edge(label=$label, node=$node)"

    private fun nodes(): Sequence<Pair<BitString, T>> {
        val parentLabel = label.s
        return when (node) {
            is HashMapNodeLeaf -> sequenceOf(parentLabel to node.value)
            is HashMapNodeFork -> (node.left.nodes() + node.right.nodes()).map { (label, value) ->
                (parentLabel + label) to value
            }
        }
    }

    fun toMap(): Map<BitString, T> = nodes().toMap()

    companion object {
        fun <T : Any> of(map: Map<BitString, T>) {
            var length = 0
            map.asSequence().sortedByDescending { (key, _) ->
                key
            }.forEach { (key, value) ->
                if (key.length > length) {

                }
                key.length
            }
        }
    }
}
