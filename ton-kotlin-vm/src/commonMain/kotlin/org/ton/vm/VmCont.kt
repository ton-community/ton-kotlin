package org.ton.vm

import org.ton.block.Maybe
import org.ton.cell.CellSlice
import org.ton.hashmap.HashMapE

public interface VmCont {
    /**
     * ```tl-b
     * vmc_std$00 cdata:VmControlData code:VmCellSlice = VmCont;
     */
    public data class VmcStd(
        val cdata: VmControlData,
        val code: VmCellSlice
    ) : VmCont

    public companion object {
        public fun std(cdata: VmControlData, code: VmCellSlice): VmcStd = VmcStd(cdata, code)
        public fun std(code: CellSlice, cdata: VmControlData? = null): VmcStd = VmcStd(
            cdata = cdata ?: VmControlData(
                nargs = Maybe.nothing(),
                stack = Maybe.nothing(),
                save = VmSaveList(HashMapE.empty()),
                cp = Maybe.nothing()
            ),
            code = VmCellSlice.fromCellSlice(code)
        )
    }
}
