package org.ton.vm.instructions

import org.ton.asm.arithmlogical.AND
import org.ton.asm.arithmlogical.OR
import org.ton.bigint.and
import org.ton.bigint.or
import org.ton.vm.VirtualMachine

internal fun VirtualMachine.registerArithmLogical() {
    register(::AND)
    register(::OR)
}

internal fun AND(vm: VirtualMachine, instruction: AND) {
    val x = vm.stack.popInt()
    val y = vm.stack.popInt()
    vm.stack.pushInt(x and y)
}

internal fun OR(vm: VirtualMachine, instruction: OR) {
    val x = vm.stack.popInt()
    val y = vm.stack.popInt()
    vm.stack.pushInt(x or y)
}
