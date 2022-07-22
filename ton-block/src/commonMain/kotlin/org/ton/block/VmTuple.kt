@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor

@JsonClassDiscriminator("@type")
@Serializable
sealed interface VmTuple {
    fun depth(): Int

    companion object {
        @JvmStatic
        fun tlbCodec(n: Int): TlbCombinator<VmTuple> = VmTupleTlbCombinator(n)
    }
}

private class VmTupleTlbCombinator(val n: Int) : TlbCombinator<VmTuple>() {
    val vmTupleNil = VmTupleNil.tlbConstructor()
    val vmTupleTcons = VmTupleTcons.tlbCodec(n)

    override val constructors: List<TlbConstructor<out VmTuple>> =
        listOf(vmTupleNil, vmTupleTcons)

    override fun getConstructor(value: VmTuple): TlbConstructor<out VmTuple> = when (value) {
        is VmTupleNil -> vmTupleNil
        is VmTupleTcons -> vmTupleTcons
    }

    override fun loadTlb(cellSlice: CellSlice): VmTuple {
        return if (n == 0) {
            vmTupleNil.loadTlb(cellSlice)
        } else {
            vmTupleTcons.loadTlb(cellSlice)
        }
    }
}


