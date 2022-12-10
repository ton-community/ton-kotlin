@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor
import kotlin.jvm.JvmStatic

@JsonClassDiscriminator("@type")
@Serializable
sealed interface VmTuple {
    fun depth(): Int

    companion object {
        @Suppress("UNCHECKED_CAST")
        @JvmStatic
        fun tlbCodec(n: Int): TlbCodec<VmTuple> = if (n == 0) {
            VmTupleNil.tlbConstructor()
        } else {
            VmTupleTcons.tlbCodec(n)
        } as TlbCodec<VmTuple>
    }
}
