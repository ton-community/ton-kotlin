@file:Suppress("OPT_IN_USAGE")

package org.ton.hashmap

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
