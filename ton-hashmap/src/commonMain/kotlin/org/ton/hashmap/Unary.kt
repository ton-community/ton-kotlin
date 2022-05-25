@file:Suppress("OPT_IN_USAGE")

package org.ton.hashmap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

fun Unary(depth: Int): Unary = Unary.of(depth)

@Serializable
@JsonClassDiscriminator("@type")
sealed class Unary {
    companion object {
        @JvmStatic
        fun of(depth: Int): Unary {
            var unary: Unary = UnaryZero
            repeat(depth) {
                unary = UnarySuccess(unary)
            }
            return unary
        }
    }
}

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
