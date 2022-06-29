@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.*

@Suppress("NOTHING_TO_INLINE")
inline fun <X> X?.toMaybe(): Maybe<X> = Maybe.of(this)

@JsonClassDiscriminator("@type")
@Serializable
sealed interface Maybe<X> {
    val value: X?

    companion object {
        @JvmStatic
        fun <X> of(value: X?): Maybe<X> = if (value != null) Just(value) else Nothing()

        @JvmStatic
        fun <X> tlbCodec(x: TlbCodec<X>): TlbCodec<Maybe<X>> = MaybeTlbCombinator(x)
    }
}

operator fun <X> Maybe.Companion.invoke(x: TlbCodec<X>): TlbCodec<Maybe<X>> = tlbCodec(x)

@SerialName("nothing")
@Serializable
class Nothing<X> : Maybe<X> {
    override val value: X? = null
    override fun hashCode(): Int = 0
    override fun equals(other: Any?): Boolean = other is Nothing<*>
}

@SerialName("just")
@Serializable
data class Just<X>(
    override val value: X
) : Maybe<X>

private class MaybeTlbCombinator<X>(
    typeCodec: TlbCodec<X>
) : TlbCombinator<Maybe<X>>() {
    @Suppress("UNCHECKED_CAST")
    private val nothingConstructor = NothingConstructor as TlbConstructor<Nothing<X>>
    private val justConstructor = JustConstructor(typeCodec)

    override val constructors: List<TlbConstructor<out Maybe<X>>> =
        listOf(
            nothingConstructor, justConstructor
        )

    override fun getConstructor(value: Maybe<X>): TlbConstructor<out Maybe<X>> = when (value) {
        is Just -> justConstructor
        is Nothing -> nothingConstructor
    }

    private object NothingConstructor : TlbConstructor<Nothing<Any>>(
        schema = "nothing\$0 {X:Type} = Maybe X;",
        id = BitString(false)
    ) {
        private val nothing = Nothing<Any>()

        override fun storeTlb(
            cellBuilder: CellBuilder,
            value: Nothing<Any>
        ) {
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): Nothing<Any> = nothing
    }

    private class JustConstructor<X>(
        val typeCodec: TlbCodec<X>
    ) : TlbConstructor<Just<X>>(
        schema = "just\$1 {X:Type} value:X = Maybe X;",
        id = ID
    ) {
        override fun storeTlb(
            cellBuilder: CellBuilder,
            value: Just<X>
        ) = cellBuilder {
            storeTlb(typeCodec, value.value)
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): Just<X> = cellSlice {
            val value = cellSlice.loadTlb(typeCodec)
            Just(value)
        }

        companion object {
            val ID = BitString(true)
        }
    }
}

