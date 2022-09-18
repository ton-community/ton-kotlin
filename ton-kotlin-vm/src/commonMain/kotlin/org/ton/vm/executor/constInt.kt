package org.ton.vm.executor

import org.ton.asm.constint.PUSHINT_16
import org.ton.asm.constint.PUSHINT_4
import org.ton.asm.constint.PUSHINT_LONG
import org.ton.asm.constint.PUSHNAN
import org.ton.vm.VmState

internal fun VmExecutor.registerConstInt() {
    register(::executePushInt8)
    register(::executePushInt16)
    register(::executePushIntLong)
    register(::executePushNan)
}

private fun executePushInt8(vmState: VmState, instruction: PUSHINT_4) = vmState {
    vmState.stack.pushTinyInt(instruction.i)
}

private fun executePushInt16(vmState: VmState, instruction: PUSHINT_16) = vmState {
    vmState.stack.pushTinyInt(instruction.xxxx)
}

private fun executePushIntLong(vmState: VmState, instruction: PUSHINT_LONG) = vmState {
    vmState.stack.pushInt(instruction.xxx)
}

private fun executePushNan(vmState: VmState, instruction: PUSHNAN) = vmState {
    vmState.stack.pushNan()
}
