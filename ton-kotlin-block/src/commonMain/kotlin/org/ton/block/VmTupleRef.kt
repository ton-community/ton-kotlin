@file:Suppress("OPT_IN_USAGE", "NOTHING_TO_INLINE")

package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.cell.*
import org.ton.tlb.*
import kotlin.jvm.JvmStatic

inline fun VmTupleRef(): VmTupleRef = VmTupleRef.of()
inline fun VmTupleRef(entry: VmStackValue): VmTupleRef = VmTupleRef.of(entry)
inline fun VmTupleRef(ref: VmTuple): VmTupleRef = VmTupleRef.of(ref)

@JsonClassDiscriminator("@type")
@Serializable
sealed interface VmTupleRef {
    fun depth(): Int

    companion object {
        @JvmStatic
        fun of(): VmTupleRef = VmTupleRefNil

        @JvmStatic
        fun of(entry: VmStackValue): VmTupleRef = VmTupleRefSingle(entry)

        @JvmStatic
        fun of(ref: VmTuple): VmTupleRef = VmTupleRefAny(ref)

        @Suppress("UNCHECKED_CAST")
        @JvmStatic
        fun tlbCodec(n: Int): TlbCodec<VmTupleRef> = when (n) {
            0 -> VmTupleRefNilTlbConstructor
            1 -> VmTupleRefSingleTlbConstructor
            else -> VmTupleRefAnyTlbConstructor(n)
        } as TlbCodec<VmTupleRef>
    }
}

@SerialName("vm_tupref_nil")
@Serializable
object VmTupleRefNil : VmTupleRef {
    override fun depth(): Int = 0

    override fun toString(): String = "vm_tupref_nil"
}

@SerialName("vm_tupref_single")
@Serializable
data class VmTupleRefSingle(
    val entry: VmStackValue
) : VmTupleRef {
    override fun depth(): Int = 1

    override fun toString(): String = "(vm_tupref_single entry:$entry)"
}

@SerialName("vm_tupref_any")
@Serializable
data class VmTupleRefAny(
    val ref: VmTuple
) : VmTupleRef {
    override fun depth(): Int = ref.depth()

    override fun toString(): String = "(vm_tupref_any ref:$ref)"
}

private object VmTupleRefNilTlbConstructor : TlbConstructor<VmTupleRefNil>(
    schema = "vm_tupref_nil\$_ = VmTupleRef 0;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder, value: VmTupleRefNil
    ) {
    }

    override fun loadTlb(cellSlice: CellSlice): VmTupleRefNil {
        return VmTupleRefNil
    }
}

private object VmTupleRefSingleTlbConstructor : TlbConstructor<VmTupleRefSingle>(
    schema = "vm_tupref_single\$_ entry:^VmStackValue = VmTupleRef 1;"
) {

    override fun storeTlb(
        cellBuilder: CellBuilder, value: VmTupleRefSingle
    ) = cellBuilder {
        storeRef {
            storeTlb(VmStackValue, value.entry)
        }
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): VmTupleRefSingle = cellSlice {
        val entry = loadRef {
            loadTlb(VmStackValue)
        }
        VmTupleRefSingle(entry)
    }
}

private class VmTupleRefAnyTlbConstructor(
    n: Int
) : TlbConstructor<VmTupleRefAny>(
    schema = "vm_tupref_any\$_ {n:#} ref:^(VmTuple (n + 2)) = VmTupleRef (n + 2);"
) {
    private val vmTupleCodec = VmTuple.tlbCodec(n - 2)

    override fun storeTlb(
        cellBuilder: CellBuilder, value: VmTupleRefAny
    ) = cellBuilder {
        storeRef {
            storeTlb(vmTupleCodec, value.ref)
        }
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): VmTupleRefAny = cellSlice {
        val ref = loadRef {
            loadTlb(vmTupleCodec)
        }
        VmTupleRefAny(ref)
    }
}
