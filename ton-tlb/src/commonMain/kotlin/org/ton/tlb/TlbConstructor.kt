package org.ton.tlb

import org.intellij.lang.annotations.Language
import org.ton.bitstring.BitString
import org.ton.tlb.exception.ParseTlbException

abstract class TlbConstructor<T : Any>(
    @Language("TL-B")
    val schema: String,
    id: BitString? = null
) : TlbCodec<T>() {
    val id = id ?: calculateId(schema)
    override fun toString(): String = schema

    companion object {
        // TODO: tests for `vm_stk_int#0201_ value:int257 = VmStackValue;`
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
                        BitString(id)
                    }
                }
                return BitString(0)
            } catch (e: Exception) {
                throw ParseTlbException("Failed to calculate id for schema: `$schema`", e)
            }
        }
    }
}
