package org.ton.vm

import org.ton.block.*

data class VmStateRunning(
    val cp: Short,
    val step: Int,
    val gas: VmGasLimits,
    val stack: Maybe<VmStack>,
    val save: VmSaveList,
    val code: VmCellSlice,
    val lib: VmLibraries,
) : VmState {
    constructor(vmStateInit: VmStateInit) : this(
        vmStateInit.cp,
        vmStateInit.step,
        vmStateInit.gas,
        vmStateInit.stack,
        vmStateInit.save,
        vmStateInit.code,
        vmStateInit.lib
    )

    override fun step(): VmState {
        TODO("Not yet implemented")
    }
}
