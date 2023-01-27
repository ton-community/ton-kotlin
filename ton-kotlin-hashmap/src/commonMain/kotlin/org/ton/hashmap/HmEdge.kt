package org.ton.hashmap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.*
import kotlin.jvm.JvmStatic

@Serializable
@SerialName("hm_edge")
public data class HmEdge<T>(
    val label: HashMapLabel,
    val node: HashMapNode<T>
) : Iterable<Pair<BitString, T>>, TlbObject {
    override fun iterator(): Iterator<Pair<BitString, T>> = nodes().iterator()

    public fun nodes(): Sequence<Pair<BitString, T>> {
        val label = label.toBitString()
        return when (node) {
            is HmnLeaf -> sequenceOf(label to node.value)
            is HmnFork -> sequence {
                yieldAll(node.left.value.nodes().map { (key, value) ->
//                    (label + false + key) to value
                    BitString.binary("${label.toBinary()}0${key.toBinary()}") to value
                })
                yieldAll(node.right.value.nodes().map { (key, value) ->
                    BitString.binary("${label.toBinary()}1${key.toBinary()}") to value
                })
            }
        }
    }

    public fun set(key: BitString, value: T): HmEdge<T> {
        check(!key.isEmpty())
        val label = label.toBitString()
        if (label == key) {
            // replace existing leaf
            return HmEdge(this.label, HmnLeaf(value))
        } else if (label.isEmpty()) {
            // 1-bit edge
            node as HmnFork<T>
            return HmEdge(this.label, node.set(key, value))
        } else {
            val labelPrefix = label.commonPrefixWith(key)
            val labelReminder = label.slice(labelPrefix.size)
            val keyReminder = key.slice(labelPrefix.size)

            if (keyReminder.isEmpty()) {
                throw IllegalArgumentException("variable length key: $key")
            } else if (!labelReminder.isEmpty() && !keyReminder.isEmpty()) {
                // forking
                val (left, right) = if (keyReminder[0]) {
                    HmEdge(HashMapLabel(labelReminder.slice(1)), node) to
                            HmEdge(HashMapLabel(keyReminder.slice(1)), HmnLeaf(value))
                } else {
                    HmEdge(HashMapLabel(keyReminder.slice(1)), HmnLeaf(value)) to
                            HmEdge(HashMapLabel(labelReminder.slice(1)), node)
                }
                return HmEdge(HashMapLabel(labelPrefix), HmnFork(left, right))
            } else if (!labelPrefix.isEmpty() && labelReminder.isEmpty() && !keyReminder.isEmpty()) {
                // next iteration
                node as HmnFork<T>
                val newNode = node.set(keyReminder, value)
                return HmEdge(HashMapLabel(labelPrefix), newNode)
            }
            throw IllegalStateException()
        }
    }

    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
        type("hm_edge") {
            field("label", label)
            field("node", node)
        }
    }

    override fun toString(): String = print().toString()

    public companion object {
        public const val ADD: Int = 0x01
        public const val REPLACE: Int = 0x02

        @JvmStatic
        public fun <X> tlbCodec(n: Int, x: TlbCodec<X>): TlbCodec<HmEdge<X>> =
            HashMapEdgeTlbConstructor(n, x)
    }
}


private class HashMapEdgeTlbConstructor<X>(
    val n: Int,
    val x: TlbCodec<X>
) : TlbConstructor<HmEdge<X>>(
    schema = "hm_edge#_ {n:#} {X:Type} {l:#} {m:#} label:(HmLabel ~l n) {n = (~m) + l} node:(HashmapNode m X) = Hashmap n X;",
    id = BitString.empty()
) {
    private val hashMapLabelCodec = HashMapLabel.tlbCodec(n)

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: HmEdge<X>
    ) {
        val l = cellBuilder.storeNegatedTlb(hashMapLabelCodec, value.label)
        val m = n - l
        cellBuilder.storeTlb(HashMapNode.tlbCodec(m, x), value.node)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): HmEdge<X> {
        val (l, label) = cellSlice.loadNegatedTlb(hashMapLabelCodec)
        val m = n - l
        val node = cellSlice.loadTlb(HashMapNode.tlbCodec(m, x))
        return HmEdge(label, node)
    }
}
