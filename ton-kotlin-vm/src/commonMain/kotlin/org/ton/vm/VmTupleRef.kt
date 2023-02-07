package org.ton.vm

import org.ton.tlb.CellRef

public interface VmTupleRef {
    /**
     * ```tl-b
     * vm_tupref_nil$_ = VmTupleRef 0;
     */
    public object VmTupRefNil : VmTupleRef

    /**
     * ```tl-b
     * vm_tupref_single$_ entry:^VmStackValue = VmTupleRef 1;
     */
    public data class VmTupRefSingle(
        val entry: CellRef<VmStackValue>
    ) : VmTupleRef

    /**
     * vm_tupref_any$_ {n:#} ref:^(VmTuple (n + 2)) = VmTupleRef (n + 2);
     */
    public data class VmTupRefAny(
        val n: Int,
        val ref: CellRef<VmTuple>
    ) : VmTupleRef
}
