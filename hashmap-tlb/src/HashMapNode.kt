@file:Suppress("OPT_IN_USAGE")

package org.ton.hashmap

import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbObject
import kotlin.jvm.JvmStatic

public sealed interface HashMapNode<T> : TlbObject {
    public companion object {
        @Suppress("UNCHECKED_CAST")
        @JvmStatic
        public fun <X> tlbCodec(n: Int, x: TlbCodec<X>): TlbCodec<HashMapNode<X>> =
            if (n == 0) {
                HmnLeaf.tlbCodec(x)
            } else {
                HmnFork.tlbCodec(n, x)
            } as TlbCodec<HashMapNode<X>>

        public fun <T> fork(
            left: HmEdge<T>,
            right: HmEdge<T>
        ): HmnFork<T> = HmnFork(left, right)

        public fun <T> leaf(value: T): HmnLeaf<T> = HmnLeaf(value)
    }
}
