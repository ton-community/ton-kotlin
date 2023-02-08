package org.ton.hashmap

import org.ton.bitstring.BitString

public interface AugmentedDictionary<X, Y>  {
    public operator fun get(key: BitString): Pair<X?, Y>
}
