package org.ton.vm

import org.ton.block.*

data class VmStateFinished(
    val cp: Short,
    val step: Int,
    val gas: VmGasLimits,
    val exit_code: Int,
    val no_gas: Boolean,
    val stack: Maybe<VmStack>,
    val save: VmSaveList,
    val lib: VmLibraries,
) : VmState {
    // Does nothing
    override fun step(): VmState = this
}
