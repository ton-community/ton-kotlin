package org.ton.vm.instructions

import org.ton.asm.codepage.SETCP
import org.ton.asm.codepage.SETCP0
import org.ton.vm.VirtualMachine

internal fun VirtualMachine.registerCodepage() {
    register(::SETCP)
    register(::SETCP0)
}

internal fun SETCP(vm: VirtualMachine, instruction: SETCP) {
    vm.setCp(instruction.nn.toInt())
}

internal fun SETCP0(vm: VirtualMachine, instruction: SETCP0) {
    vm.setCp(0)
}

private fun VirtualMachine.setCp(value: Int) {
    cp = value
    if (cp != 0) return

    registerArithmLogical()
    registerCompareInt()
    registerConstInt()
    registerContConditional()
    registerContData()
    registerContRegisters()
    registerStackBasic()
}
