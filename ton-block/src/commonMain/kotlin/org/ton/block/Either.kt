@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.*

@Suppress("NOTHING_TO_INLINE")
inline fun <X, Y> Pair<X?, Y?>.toEither(): Either<X, Y> = Either.of(first, second)

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

    companion object {
        @JvmStatic
        fun <X, Y> of(left: X?, right: Y?): Either<X, Y> {
            if (left != null) {
                return Left(left)
            }
            if (right != null) {
                return Right(right)
            }
            throw IllegalArgumentException("first & second == null; At least one element must be non-null")
        }

        @JvmStatic
        fun <X : Any, Y : Any> tlbCodec(x: TlbCodec<X>, y: TlbCodec<Y>): TlbCodec<Either<X, Y>> =
            EitherTlbCombinator(x, y)
    }
}

private class EitherTlbCombinator<X : Any, Y : Any>(x: TlbCodec<X>, y: TlbCodec<Y>) : TlbCombinator<Either<X, Y>>() {
    private val leftCodec by lazy {
        LeftTlbConstructor<X, Y>(x)
    }
    private val rightCodec by lazy {
        RightTlbConstructor<X, Y>(y)
    }

    override val constructors: List<TlbConstructor<out Either<X, Y>>> = listOf(
        leftCodec, rightCodec
    )

    override fun getConstructor(value: Either<X, Y>): TlbConstructor<out Either<X, Y>> = when (value) {
        is Either.Left -> leftCodec
        is Either.Right -> rightCodec
    }

    class LeftTlbConstructor<X : Any, Y : Any>(val x: TlbCodec<X>) : TlbConstructor<Either.Left<X, Y>>(
        schema = "left\$0 {X:Type} {Y:Type} value:X = Either X Y;"
    ) {
        override fun storeTlb(
            cellBuilder: CellBuilder, value: Either.Left<X, Y>
        ) = cellBuilder {
            storeTlb(x, value.value)
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): Either.Left<X, Y> = cellSlice {
            val value = loadTlb(x)
            Either.Left(value)
        }
    }

    class RightTlbConstructor<X : Any, Y : Any>(val y: TlbCodec<Y>) : TlbConstructor<Either.Right<X, Y>>(
        schema = "right\$1 {X:Type} {Y:Type} value:Y = Either X Y;"
    ) {
        override fun storeTlb(
            cellBuilder: CellBuilder,
            value: Either.Right<X, Y>
        ) = cellBuilder {
            storeTlb(y, value.value)
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): Either.Right<X, Y> = cellSlice {
            val value = loadTlb(y)
            Either.Right(value)
        }
    }
}
