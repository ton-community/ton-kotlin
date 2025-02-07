package org.ton.hashmap

import org.ton.kotlin.bitstring.BitString

public interface Dictionary<X> {
    public interface Leaf<out X> {
        public val value: X
    }

    public interface Entry<out X> {
        public val key: BitString
        public val leaf: Leaf<X>

        public operator fun component1(): BitString = key
        public operator fun component2(): X = leaf.value
    }
}
