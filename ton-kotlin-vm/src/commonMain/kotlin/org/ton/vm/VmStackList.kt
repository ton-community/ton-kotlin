package org.ton.vm

import org.ton.tlb.CellRef
import kotlin.jvm.JvmStatic

public interface VmStackList {
    public val n: Int

    /**
     * ```tl-b
     * vm_stk_cons#_ {n:#} rest:^(VmStackList n) tos:VmStackValue = VmStackList (n + 1);
     */
    public data class VmStkCons(
        override val n: Int,
        val rest: CellRef<VmStackList>,
        val tos: VmStackValue
    ) : VmStackList {
        public fun loadRes(): VmStackList = rest.value
    }

    /**
     * ```tl-b
     * vm_stk_nil#_ = VmStackList 0;
     */
    public object VmStkNil : VmStackList {
        override val n: Int get() = 0
    }

    public companion object {
        @JvmStatic
        public fun fromIterable(iterable: Iterable<VmStackValue>): VmStackList {
            var list: VmStackList = VmStkNil
            for (value in iterable) {
                list = VmStkCons(list.n, CellRef(list), value)
            }
            return list
        }
    }
}

public inline fun Iterable<VmStackValue>.toVmStackList(): VmStackList = VmStackList.fromIterable(this)
