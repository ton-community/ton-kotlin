package org.ton.hashmap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.tlb.TlbPrettyPrinter

@Serializable
@SerialName("hml_long")
public data class HashMapLabelLong(
    val n: Int,
    override val s: BitString
) : HashMapLabel {
    public constructor(s: BitString) : this(s.size, s)

    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer.type("hml_long") {
        field("n", n)
        field("s", s)
    }

    override fun toString(): String = print().toString()
}
