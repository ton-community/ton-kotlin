package org.ton.vm.instructions

import org.ton.asm.constint.*
import org.ton.vm.VirtualMachine

internal fun VirtualMachine.registerConstInt() {
    register(::ZERO)
    register(::ONE)
    register(::TWO)
    register(::TEN)
    register(::TRUE)
    register(::PUSHINT_4)
    register(::PUSHINT_8)
    register(::PUSHINT_16)
    register(::PUSHINT_LONG)
}

internal fun ZERO(vm: VirtualMachine, instruction: ZERO) {
    vm.stack.pushTinyInt(0)
}

internal fun ONE(vm: VirtualMachine, instruction: ONE) {
    vm.stack.pushTinyInt(1)
}

internal fun TWO(vm: VirtualMachine, instruction: TWO) {
    vm.stack.pushTinyInt(2)
}

internal fun TEN(vm: VirtualMachine, instruction: TEN) {
    vm.stack.pushTinyInt(10)
}

internal fun TRUE(vm: VirtualMachine, instruction: TRUE) {
    vm.stack.pushBool(true)
}

internal fun PUSHINT_4(vm: VirtualMachine, instruction: PUSHINT_4) {
    vm.stack.pushTinyInt(instruction.x)
}

internal fun PUSHINT_16(vm: VirtualMachine, instruction: PUSHINT_16) {
    vm.stack.pushTinyInt(instruction.xxxx)
}

internal fun PUSHINT_8(vm: VirtualMachine, instruction: PUSHINT_8) {
    vm.stack.pushTinyInt(instruction.xx)
}

internal fun PUSHINT_LONG(vm: VirtualMachine, instruction: PUSHINT_LONG) {
    vm.stack.pushInt(instruction.xxx)
}
