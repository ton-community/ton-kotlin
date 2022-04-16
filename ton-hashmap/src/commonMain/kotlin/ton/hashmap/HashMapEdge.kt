package ton.hashmap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("hm_edge")
data class HashMapEdge<T>(
    val label: HashMapLabel,
    val node: HashMapNode<T>
) {
    override fun toString(): String = "hm_edge(label=$label, node=$node)"
}