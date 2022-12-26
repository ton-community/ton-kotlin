package org.ton.vm.instructions

import org.ton.asm.constdata.PUSHCONT
import org.ton.asm.constdata.PUSHCONT_SHORT
import org.ton.block.VmContStd
import org.ton.cell.CellSlice
import org.ton.vm.VirtualMachine

internal fun VirtualMachine.registerContData() {
    register(::PUSHCONT)
    register(::PUSHCONT_SHORT)
}

internal fun PUSHCONT(vm: VirtualMachine, instruction: PUSHCONT) {
    val bits = instruction.ssss
    val refs = instruction.c
    val cont = VmContStd(
        code = CellSlice(bits, refs),
        cp = vm.cp
    )
    vm.stack.pushCont(cont)
}

internal fun PUSHCONT_SHORT(vm: VirtualMachine, instruction: PUSHCONT_SHORT) {
    val continuation = VmContStd(CellSlice(instruction.ssss), vm.cp)
    vm.stack.pushCont(continuation)
}
