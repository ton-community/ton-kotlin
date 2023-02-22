package org.ton.hashmap

import org.ton.bitstring.BitString

public interface AugmentedDictionary<X, Y> : Iterable<AugmentedDictionary.Entry<X, Y>> {
    public operator fun get(key: BitString): Pair<X?, Y>

    override fun iterator(): Iterator<Entry<X, Y>>

    public interface Leaf<out X, out Y> : Dictionary.Leaf<X> {
        public override val value: X
        public val extra: Y

        public operator fun component1(): X = value
        public operator fun component2(): Y = extra
    }

    public interface Entry<out X, out Y> : Dictionary.Entry<X> {
        override val key: BitString
        override val leaf: Leaf<X, Y>

        public operator fun component3(): Y = leaf.extra
    }
}
