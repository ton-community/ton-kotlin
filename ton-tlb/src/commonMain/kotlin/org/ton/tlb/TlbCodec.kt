package org.ton.tlb

import org.intellij.lang.annotations.Language
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.crypto.crc32

interface TlbEncoder<T> {
    fun encode(
            cellBuilder: CellBuilder,
            value: T,
            typeParam: TlbEncoder<Any>? = null,
            param: Int = 0,
            negativeParam: ((Int) -> Unit)? = null
    )
}

interface TlbDecoder<T> {
    fun decode(
            cellSlice: CellSlice,
            typeParam: TlbDecoder<Any>? = null,
            param: Int = 0,
            negativeParam: ((Int) -> Unit)? = null
    ): T
}

abstract class TlbCodec<T> : TlbEncoder<T>, TlbDecoder<T> {
    abstract override fun toString(): String
}

abstract class TlbConstructor<T>(
    @Language("TL-B")
    val schema: String
) : TlbCodec<T>() {
    val id: Int = crc32(schema)

    override fun toString(): String = schema
}

abstract class TlbCombinator<T>(
        val constructors: List<TlbConstructor<out T>>
) : TlbCodec<T>() {
    override fun toString(): String = constructors.joinToString("\n")
}