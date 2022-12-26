package org.ton.vm.instructions

import org.ton.asm.contconditional.IFJMP
import org.ton.vm.VirtualMachine

internal fun VirtualMachine.registerContConditional() {
    register(::IFJMP)
}

internal fun IFJMP(vm: VirtualMachine, instruction: IFJMP) {
    val cont = vm.stack.popCont()
    if (vm.stack.popBool()) {
        vm.jump(cont)
    }
}
