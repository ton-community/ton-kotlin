package org.ton.hashmap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.tlb.TlbPrettyPrinter

@Serializable
@SerialName("hml_short")
public data class HashMapLabelShort(
    val len: Unary,
    override val s: BitString
) : HashMapLabel {
    public constructor(s: BitString) : this(Unary(s.size), s)

    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter {
        return printer.type("hml_short") {
            field("len", len)
            field("s", s)
        }
    }

    override fun toString(): String = print().toString()
}
