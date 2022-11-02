package org.ton.vm.executor

import org.ton.asm.constint.*
import org.ton.vm.VmState

internal fun VmExecutor.registerConstInt() {
    register(::executePushInt4)
    register(::executePushInt8)
    register(::executePushInt16)
    register(::executePushIntLong)
    register(::executePushNan)
}

private fun executePushInt4(vmState: VmState, instruction: PUSHINT_4) = vmState {
    vmState.stack.pushTinyInt(instruction.i)
}

private fun executePushInt8(vmState: VmState, instruction: PUSHINT_8) = vmState {
    vmState.stack.pushTinyInt(instruction.xx)
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
