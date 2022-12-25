package org.ton.hashmap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString

@Serializable
@SerialName("hml_short")
public data class HashMapLabelShort(
    val len: Unary,
    override val s: BitString
) : HashMapLabel {
    public constructor(s: BitString) : this(Unary(s.size), s)

    override fun toString(): String = "(hml_short len:$len s:$s)"
}
