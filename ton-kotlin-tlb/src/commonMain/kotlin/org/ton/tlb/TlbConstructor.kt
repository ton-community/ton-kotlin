package org.ton.tlb

import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.exception.ParseTlbException
import org.ton.tlb.providers.TlbConstructorProvider
import kotlin.reflect.KClass

abstract class AbstractTlbConstructor<T>(
    val schema: String,
    id: BitString? = null,
) {
    val id by lazy(LazyThreadSafetyMode.PUBLICATION) {
        id ?: calculateId(formatSchema(schema))
    }
    override fun toString(): String = schema

    companion object {
        fun calculateId(schema: String): BitString {
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

        fun formatSchema(schema: String): String {
            return schema.replace(Regex("\\s+"), " ").trim()
                .replace("(","")
                .replace(")","")
                .replace(";","")
        }
    }
}

abstract class TlbConstructor<T : Any>(
    schema: String,
    id: BitString? = null,
) : AbstractTlbConstructor<T>(schema, id), TlbCodec<T>, TlbConstructorProvider<T> {
    override fun tlbConstructor(): TlbConstructor<T> = this
}

class ObjectTlbConstructor<T : Any>(
    val instance: T,
    schema: String,
    id: BitString? = null,
) : TlbConstructor<T>(schema, id) {
    override fun storeTlb(cellBuilder: CellBuilder, value: T) {
    }
    override fun loadTlb(cellSlice: CellSlice): T = instance
}

abstract class TlbNegatedConstructor<T : Any>(
    schema: String,
    id: BitString? = null
) : TlbConstructor<T>(schema, id), TlbNegatedCodec<T> {
    override fun storeTlb(cellBuilder: CellBuilder, value: T) {
        storeNegatedTlb(cellBuilder, value)
    }

    override fun loadTlb(cellSlice: CellSlice): T = loadNegatedTlb(cellSlice).second
}

inline fun <reified T:Any>TlbConstructor<T>.asTlbCombinator() = asTlbCombinator(T::class)

fun <T:Any> TlbConstructor<T>.asTlbCombinator(clazz: KClass<T>): TlbCombinator<T> = object : TlbCombinator<T>(
    clazz,
    clazz to this@asTlbCombinator
) {
}
