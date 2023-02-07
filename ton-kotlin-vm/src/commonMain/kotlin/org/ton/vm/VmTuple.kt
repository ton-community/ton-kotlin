package org.ton.vm

import org.ton.tlb.CellRef

public interface VmTuple {
    /**
     * ```tl-b
     * vm_tuple_nil$_ = VmTuple 0;
     */
    public object VmTupleNil : VmTuple

    /**
     * ```tl-b
     * vm_tuple_tcons$_ {n:#} head:(VmTupleRef n) tail:^VmStackValue = VmTuple (n + 1);
     */
    public data class VmTupleTcons(
        val n: Int,
        val head: VmTupleRef,
        val tail: CellRef<VmStackValue>
    ) : VmTuple
}
