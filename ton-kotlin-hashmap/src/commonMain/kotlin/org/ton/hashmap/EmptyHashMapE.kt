package org.ton.hashmap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.tlb.TlbPrettyPrinter

@Serializable
@SerialName("hme_empty")
public class EmptyHashMapE<out T> : HashMapE<T> {
    override fun nodes(): Sequence<Pair<BitString, Nothing>> = emptySequence()

    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter =
        printer.type("hme_empty")

    override fun toString(): String = print().toString()
}
