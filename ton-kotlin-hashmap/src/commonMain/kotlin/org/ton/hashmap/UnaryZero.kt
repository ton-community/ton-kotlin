package org.ton.hashmap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("unary_zero")
public object UnaryZero : Unary() {
    override fun toString(): String = "unary_zero"
}
