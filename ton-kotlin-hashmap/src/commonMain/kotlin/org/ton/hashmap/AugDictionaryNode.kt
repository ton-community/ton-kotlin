@file:Suppress("OPT_IN_USAGE")

package org.ton.hashmap

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbObject
import kotlin.jvm.JvmStatic

@JsonClassDiscriminator("@type")
@Serializable
public sealed interface AugDictionaryNode<X, Y> : TlbObject {
    public val extra: Y

    public companion object {
        @Suppress("UNCHECKED_CAST")
        @JvmStatic
        public fun <X, Y> tlbCodec(
            n: Int,
            x: TlbCodec<X>,
            y: TlbCodec<Y>
        ): TlbCodec<AugDictionaryNode<X, Y>> {
            val codec = if (n <= 0) {
                AugDictionaryNodeLeaf.tlbCodec(x, y)
            } else {
                AugDictionaryNodeFork.tlbCodec(n, x, y)
            }
            return codec as TlbCodec<AugDictionaryNode<X, Y>>
        }
    }
}
