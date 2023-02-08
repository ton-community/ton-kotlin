package org.ton.hashmap

import org.ton.bitstring.BitString

public interface DictionaryAug<X, Y> : Iterable<Triple<BitString, X, Y>> {
    override fun iterator(): Iterator<Triple<BitString, X, Y>> = nodes().iterator()

    public fun nodes(): Sequence<Triple<BitString, X, Y>>
}
