package org.ton.vm

import org.ton.block.*
import org.ton.hashmap.HashMapE

data class VmStateInit(
    val cp: Short = 0,
    val step: Int = 0,
    val gas: VmGasLimits,
    val stack: Maybe<VmStack>,
    val save: VmSaveList,
    val code: VmCellSlice,
    val lib: VmLibraries = VmLibraries(HashMapE.of())
) : VmState {
    override fun step(): VmState = VmStateRunning(this).step()
}
