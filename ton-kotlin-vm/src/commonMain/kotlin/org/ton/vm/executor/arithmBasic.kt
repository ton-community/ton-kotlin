package org.ton.vm.executor

import org.ton.asm.arithmbasic.*
import org.ton.block.VmStackNumber
import org.ton.block.VmStackValue
import org.ton.vm.VmState

internal fun VmExecutor.registerArithmBasic() {
    register(::executeAdd)
    register(::executeSub)
    register(::executeSubr)
    register(::executeNegate)
    register(::executeInc)
    register(::executeDec)
    register(::executeAddConst)
    register(::executeMulConst)
    register(::executeMul)
}

private fun executeAdd(vmState: VmState, instruction: ADD) = vmState {
    val x = vmState.stack.pop() as VmStackNumber
    val y = vmState.stack.pop() as VmStackNumber
    vmState.stack.push(x + y)
}

private fun executeSub(vmState: VmState, instruction: SUB) = vmState {
    val x = vmState.stack.pop() as VmStackNumber
    val y = vmState.stack.pop() as VmStackNumber
    vmState.stack.push(x - y)
}

private fun executeSubr(vmState: VmState, instruction: SUBR) = vmState {
    val x = vmState.stack.pop() as VmStackNumber
    val y = vmState.stack.pop() as VmStackNumber
    vmState.stack.push(y - x)
}

private fun executeNegate(vmState: VmState, instruction: NEGATE) = vmState {
    val x = vmState.stack.pop() as VmStackNumber
    vmState.stack.push(-x)
}

private fun executeInc(vmState: VmState, instruction: INC) = vmState {
    val x = vmState.stack.pop() as VmStackNumber
    vmState.stack.push(x + VmStackValue(1))
}

private fun executeDec(vmState: VmState, instruction: DEC) = vmState {
    val x = vmState.stack.pop() as VmStackNumber
    vmState.stack.push(x - VmStackValue(1))
}

private fun executeAddConst(vmState: VmState, instruction: ADDCONST) = vmState {
    val x = vmState.stack.pop() as VmStackNumber
    vmState.stack.push(x - VmStackValue(instruction.cc))
}

private fun executeMulConst(vmState: VmState, instruction: MULCONST) = vmState {
    val x = vmState.stack.pop() as VmStackNumber
    vmState.stack.push(x * VmStackValue(instruction.cc))
}

private fun executeMul(vmState: VmState, instruction: MUL) = vmState {
    val x = vmState.stack.pop() as VmStackNumber
    val y = vmState.stack.pop() as VmStackNumber
    vmState.stack.push(x * y)
}
