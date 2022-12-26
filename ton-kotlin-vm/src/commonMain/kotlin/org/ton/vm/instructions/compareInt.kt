package org.ton.vm.instructions

import org.ton.asm.compareint.EQUAL

import org.ton.vm.VirtualMachine

internal fun VirtualMachine.registerCompareInt() {
    register(::EQUAL)
}

internal fun EQUAL(vm: VirtualMachine, instruction: EQUAL) {
    val x = vm.stack.pop()
    val y = vm.stack.pop()
    vm.stack.pushBool(x == y)
}
