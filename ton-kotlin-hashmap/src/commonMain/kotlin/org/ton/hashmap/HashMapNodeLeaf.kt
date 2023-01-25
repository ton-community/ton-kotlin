package org.ton.hashmap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.tlb.TlbPrettyPrinter

@Serializable
@SerialName("hmn_leaf")
public data class HashMapNodeLeaf<out T>(
    val value: T
) : HashMapNode<T> {
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter {
        return printer.type("hmn_leaf") {
            field("value", value as Any)
        }
    }

    override fun toString(): String = "(hmn_leaf value:$value)"
}
