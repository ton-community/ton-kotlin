package org.ton.hashmap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("unary_succ")
data class UnarySuccess(
    val x: Unary
) : Unary() {
    override fun toString() = "unary_succ(x=$x)"
}
