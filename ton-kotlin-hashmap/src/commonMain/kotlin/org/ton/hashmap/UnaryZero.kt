package org.ton.hashmap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("unary_zero")
object UnaryZero : Unary() {
    override fun toString() = "unary_zero"
}
