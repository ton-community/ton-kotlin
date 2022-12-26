@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tlb.TlbCodec
import kotlin.jvm.JvmStatic

@JsonClassDiscriminator("@type")
@Serializable
public sealed interface VmTuple : Sequence<VmStackValue> {
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
