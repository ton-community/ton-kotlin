@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@JsonClassDiscriminator("@type")
@Serializable
sealed interface Either<X, Y> {
    val x: X?
    val y: Y?

    fun toPair(): Pair<X?, Y?> = x to y

    @SerialName("left")
    @Serializable
    class Left<X, Y>(
        val value: X
    ) : Either<X, Y> {
        override val x: X? = value
        override val y: Y? = null

        operator fun component1() = x
        operator fun component2() = y

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Left<*, *>

            if (value != other.value) return false

            return true
        }

        override fun hashCode(): Int = value?.hashCode() ?: 0

        override fun toString(): String = "Left(value=$value)"
    }

    @SerialName("right")
    @Serializable
    class Right<X, Y>(
        val value: Y
    ) : Either<X, Y> {
        override val x: X? = null
        override val y: Y? = value

        operator fun component1() = x
        operator fun component2() = y

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Right<*, *>

            if (value != other.value) return false

            return true
        }

        override fun hashCode(): Int = value?.hashCode() ?: 0

        override fun toString(): String = "Right(value=$value)"
    }
}

fun <X, Y> Pair<X?, Y?>.toEither(): Either<X, Y> {
    val left = first
    if (left != null) {
        return Either.Left(left)
    }
    val right = second
    if (right != null) {
        return Either.Right(right)
    }
    throw IllegalArgumentException("first & second == null; At least one element must be non-null")
}
