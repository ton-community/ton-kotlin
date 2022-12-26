package org.ton.vm.instructions

import org.ton.asm.contregisters.PUSHCTR
import org.ton.asm.contregisters.PUSHCTRX
import org.ton.asm.contregisters.PUSHROOT
import org.ton.vm.VirtualMachine

internal fun VirtualMachine.registerContRegisters() {
    register(::PUSHROOT)
    register(::PUSHCTR)
    register(::PUSHCTRX)
}

internal fun PUSHROOT(vm: VirtualMachine, instruction: PUSHROOT) =
    pushCtr(vm, 4)

internal fun PUSHCTR(vm: VirtualMachine, instruction: PUSHCTR) =
    pushCtr(vm, instruction.i.toInt())

internal fun PUSHCTRX(vm: VirtualMachine, instruction: PUSHCTRX) =
    pushCtr(vm, vm.stack.popTinyInt().toInt())

private fun pushCtr(vm: VirtualMachine, cr: Int) {
    val value = vm.controlRegistries[cr]
    if (value != null) {
        vm.stack.push(value)
    } else {
        vm.stack.pushNull()
    }
}
