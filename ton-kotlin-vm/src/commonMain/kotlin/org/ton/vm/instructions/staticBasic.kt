package org.ton.vm.instructions

import org.ton.asm.stackbasic.NOP
import org.ton.asm.stackbasic.SWAP
import org.ton.block.Maybe
import org.ton.vm.VmState
import org.ton.vm.VmStateException
import org.ton.vm.VmStateRunning

internal fun executeNop(state: VmStateRunning, instruction: NOP): VmState = state

internal fun executeSwap(state: VmStateRunning, instruction: SWAP): VmState {
    val stack = state.stack.value?.toMutableVmStack()

    return if (stack == null || stack.depth < 2) { // None or too few elements on stack
        VmStateException.of(state, VmStateException.STACK_UNDERFLOW)
    } else {
        val s0 = stack.pop()
        val s1 = stack.pop()

        stack.push(s0)
        stack.push(s1)

        state.copy(stack = Maybe.of(stack))
    }
}
