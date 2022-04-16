@file:Suppress("OPT_IN_USAGE")

package org.ton.hashmap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@Serializable
@JsonClassDiscriminator("@type")
sealed class Unary

@Serializable
@SerialName("unary_succ")
data class UnarySuccess(
    val x: Unary
) : Unary() {
    override fun toString() = "unary_succ(x=$x)"
}

@Serializable
@SerialName("unary_zero")
object UnaryZero : Unary() {
    override fun toString() = "unary_zero"
}

fun Unary(x: Int): UnarySuccess {
    var unary = UnarySuccess(UnaryZero)
    repeat(x - 1) {
        unary = UnarySuccess(unary)
    }
    return unary
}