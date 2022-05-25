package org.ton.block.tlb

import org.ton.block.Either
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.*

fun <X : Any, Y : Any> Either.Companion.tlbCodec(x: TlbCodec<X>, y: TlbCodec<Y>): TlbCodec<Either<X, Y>> =
    EitherTlbCombinator(x, y)

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
        override fun encode(
            cellBuilder: CellBuilder, value: Either.Left<X, Y>, param: Int, negativeParam: (Int) -> Unit
        ) = cellBuilder {
            storeTlb(value.value, x)
        }

        override fun decode(
            cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit
        ): Either.Left<X, Y> = cellSlice {
            val value = loadTlb(x)
            Either.Left(value)
        }
    }

    class RightTlbConstructor<X : Any, Y : Any>(val y: TlbCodec<Y>) : TlbConstructor<Either.Right<X, Y>>(
        schema = "right\$1 {X:Type} {Y:Type} value:Y = Either X Y;"
    ) {
        override fun encode(
            cellBuilder: CellBuilder,
            value: Either.Right<X, Y>,
            param: Int,
            negativeParam: (Int) -> Unit
        ) = cellBuilder {
            storeTlb(value.value, y)
        }

        override fun decode(
            cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit
        ): Either.Right<X, Y> = cellSlice {
            val value = loadTlb(y)
            Either.Right(value)
        }
    }
}
