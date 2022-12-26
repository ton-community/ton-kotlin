package org.ton.vm.instructions

import org.ton.asm.stackbasic.*
import org.ton.vm.VirtualMachine

internal fun VirtualMachine.registerStackBasic() {
    register(::NOP)
    register(::SWAP)
    register(::OVER)
    register(::XCHG_0I)
    register(::XCHG_IJ)
    register(::XCHG_0I_LONG)
    register(::XCHG_1I)
    register(::PUSH)
    register(::DUP)
    register(::POP)
    register(::DROP)
    register(::NIP)
}

internal fun NOP(vm: VirtualMachine, instruction: NOP) {
    // Do nothing
}

internal fun SWAP(vm: VirtualMachine, instruction: SWAP) {
    vm.stack.swap()
}

internal fun XCHG_0I(vm: VirtualMachine, instruction: XCHG_0I) {
    vm.stack.interchange(0, instruction.i.toInt())
}

internal fun XCHG_IJ(vm: VirtualMachine, instruction: XCHG_IJ) {
    vm.stack.interchange(instruction.i.toInt(), instruction.j.toInt())
}

internal fun XCHG_0I_LONG(vm: VirtualMachine, instruction: XCHG_0I_LONG) {
    vm.stack.interchange(0, instruction.ii.toInt())
}

internal fun XCHG_1I(vm: VirtualMachine, instruction: XCHG_1I) {
    vm.stack.interchange(1, instruction.i.toInt())
}

internal fun PUSH(vm: VirtualMachine, instruction: PUSH) {
    val value = vm.stack[instruction.i.toInt()]
    vm.stack.push(value)
}

internal fun DUP(vm: VirtualMachine, instruction: DUP) {
    val value = vm.stack[0]
    vm.stack.push(value)
}

internal fun OVER(vm: VirtualMachine, instruction: OVER) {
    val x = vm.stack.pop()
    val y = vm.stack.pop()
    vm.stack.push(x)
    vm.stack.push(y)
    vm.stack.push(x)
}

internal fun POP(vm: VirtualMachine, instruction: POP) {
    vm.stack.pop(instruction.i.toInt())
}

internal fun DROP(vm: VirtualMachine, instruction: DROP) {
    vm.stack.pop()
}

internal fun NIP(vm: VirtualMachine, instruction: NIP) {
    vm.stack.pop(1)
}
