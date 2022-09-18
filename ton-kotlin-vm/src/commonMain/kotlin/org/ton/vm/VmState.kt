package org.ton.vm

import org.ton.block.MutableVmStack
import org.ton.block.VmCont
import org.ton.block.VmContQuitExc

data class VmState(
    var stack: MutableVmStack = MutableVmStack(),
    var controlRegisters: VmControlRegistry = VmControlRegistry(),
    var currentContinuation: VmCont = VmContQuitExc,
    var currentCodepage: Short = 0,
    var gas: VmGas = VmGas()
) {
    operator fun invoke(block: VmState.() -> Unit) = apply(block)
}
