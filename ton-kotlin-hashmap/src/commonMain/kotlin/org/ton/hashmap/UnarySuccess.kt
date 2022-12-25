package org.ton.hashmap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("unary_succ")
public data class UnarySuccess(
    val x: Unary
) : Unary() {
    override fun toString(): String = "(unary_succ\nx:$x)"
}
