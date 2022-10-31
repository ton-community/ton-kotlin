package org.ton.tlb

import org.intellij.lang.annotations.Language
import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.exception.ParseTlbException
import org.ton.tlb.providers.TlbConstructorProvider
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.createType

abstract class AbstractTlbConstructor<T>(
    @Language("TL-B")
    val schema: String,
    id: BitString? = null,
    val type: KType? = null
) {
    val id = id ?: calculateId(schema)
    override fun toString(): String = schema

    companion object {
        fun calculateId(@Language("TL-B") schema: String): BitString {
            if (schema.isEmpty()) return BitString(0)
            try {
                val prefix = schema.split(" ").first()
                if (prefix.contains('$')) {
                    val (_, id) = prefix.split('$')
                    if (id != "_") {
                        return BitString.binary(id)
                    }
                } else if (prefix.contains('#')) {
                    val (_, id) = prefix.split('#')
                    if (id != "_") {
                        return BitString(id)
                    }
                }
                return BitString(0)
            } catch (e: Exception) {
                throw ParseTlbException("Failed to calculate id for schema: `$schema`", e)
            }
        }
    }
}

abstract class TlbConstructor<T : Any>(
    @Language("TL-B")
    schema: String,
    id: BitString? = null,
    type: KType?
) : AbstractTlbConstructor<T>(schema, id, type), TlbCodec<T>, TlbConstructorProvider<T> {
    constructor(
        @Language("TL-B")
        schema: String,
        id: BitString? = null,
        type: KClass<T>? = null
    ) : this(schema, id, type?.createType())

    abstract override fun storeTlb(cellBuilder: CellBuilder, value: T)
    abstract override fun loadTlb(cellSlice: CellSlice): T

    override fun tlbConstructor(): TlbConstructor<T> = this

    fun asTlbCombinator(): TlbCombinator<T> = object : TlbCombinator<T>() {
        override val constructors: List<TlbConstructor<out T>> = listOf(this@TlbConstructor)

        override fun getConstructor(value: T): TlbConstructor<out T> = this@TlbConstructor
    }
}

class ObjectTlbConstructor<T : Any>(
    @Language("TL-B")
    schema: String,
    val instance: T,
    id: BitString? = null,
) : TlbConstructor<T>(schema, id) {
    override fun storeTlb(cellBuilder: CellBuilder, value: T) {
    }
    override fun loadTlb(cellSlice: CellSlice): T = instance
}

abstract class TlbNegatedConstructor<T>(
    @Language("TL-B")
    schema: String,
    id: BitString? = null
) : AbstractTlbConstructor<T>(schema, id), TlbNegatedCodec<T>
