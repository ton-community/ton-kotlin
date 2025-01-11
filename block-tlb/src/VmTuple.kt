@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.Serializable

import org.ton.tlb.TlbCodec
import kotlin.jvm.JvmStatic


@Serializable
public sealed interface VmTuple {
    public fun depth(): Int

    public companion object {
        @Suppress("UNCHECKED_CAST")
        @JvmStatic
        public fun tlbCodec(n: Int): TlbCodec<VmTuple> = if (n == 0) {
            VmTupleNil.tlbConstructor()
        } else {
            VmTupleTcons.tlbCodec(n)
        } as TlbCodec<VmTuple>
    }
}
