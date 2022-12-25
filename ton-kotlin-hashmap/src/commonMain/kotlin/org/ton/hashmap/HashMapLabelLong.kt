package org.ton.hashmap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString

@Serializable
@SerialName("hml_long")
public data class HashMapLabelLong(
    val n: Int,
    override val s: BitString
) : HashMapLabel {
    public constructor(s: BitString) : this(s.size, s)

    override fun toString(): String = "(hml_long\nn:$n s:$s)"
}
