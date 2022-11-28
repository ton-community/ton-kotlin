package org.ton.vm.instructions

import org.ton.asm.arithmbasic.*
import org.ton.block.Maybe
import org.ton.block.VmStackNumber
import org.ton.block.VmStackValue
import org.ton.vm.VmState
import org.ton.vm.VmStateException
import org.ton.vm.VmStateRunning

internal fun executeAdd(state: VmStateRunning, instruction: ADD): VmState {
    val stack = state.stack.value?.toMutableVmStack()

    return if (stack == null || stack.depth < 2) { // None or too few elements on stack
        VmStateException.of(state, VmStateException.STACK_UNDERFLOW)
    } else {
        val x = stack.pop() as? VmStackNumber
        val y = stack.pop() as? VmStackNumber

        if (x == null || y == null) { // Both need to be numbers
            VmStateException.of(state, VmStateException.TYPE_CHECK_ERROR)
        } else {
            val result = x + y
            // TODO: Check for integer overflow
            stack.push(result)
            state.copy(stack = Maybe.of(stack))
        }
    }
}

internal fun executeSub(state: VmStateRunning, instruction: SUB): VmState {
    val stack = state.stack.value?.toMutableVmStack()

    return if (stack == null || stack.depth < 2) { // None or too few elements on stack
        VmStateException.of(state, VmStateException.STACK_UNDERFLOW)
    } else {
        val x = stack.pop() as? VmStackNumber
        val y = stack.pop() as? VmStackNumber

        if (x == null || y == null) { // Both need to be numbers
            VmStateException.of(state, VmStateException.TYPE_CHECK_ERROR)
        } else {
            val result = x - y
            // TODO: Check for integer overflow
            stack.push(result)
            state.copy(stack = Maybe.of(stack))
        }
    }
}

internal fun executeSubr(state: VmStateRunning, instruction: SUBR): VmState {
    val stack = state.stack.value?.toMutableVmStack()

    return if (stack == null || stack.depth < 2) { // None or too few elements on stack
        VmStateException.of(state, VmStateException.STACK_UNDERFLOW)
    } else {
        val x = stack.pop() as? VmStackNumber
        val y = stack.pop() as? VmStackNumber

        if (x == null || y == null) { // Both need to be numbers
            VmStateException.of(state, VmStateException.TYPE_CHECK_ERROR)
        } else {
            val result = y - x
            // TODO: Check for integer overflow
            stack.push(result)
            state.copy(stack = Maybe.of(stack))
        }
    }
}

internal fun executeNegate(state: VmStateRunning, instruction: NEGATE): VmState {
    val stack = state.stack.value?.toMutableVmStack()

    return if (stack == null || stack.depth < 1) { // None or too few elements on stack
        VmStateException.of(state, VmStateException.STACK_UNDERFLOW)
    } else {
        val x = stack.pop() as? VmStackNumber

        if (x == null) { // Has to be a number
            VmStateException.of(state, VmStateException.TYPE_CHECK_ERROR)
        } else {
            val result = -x
            // TODO: Has to trigger an integer overflow exception when x = -2^256
            stack.push(result)
            state.copy(stack = Maybe.of(stack))
        }
    }
}

internal fun executeInc(state: VmStateRunning, instruction: INC): VmState =
    executeAddConst(state, ADDCONST(1.toByte()))


internal fun executeDec(state: VmStateRunning, instruction: DEC): VmState =
    executeAddConst(state, ADDCONST((-1).toByte()))

internal fun executeAddConst(state: VmStateRunning, instruction: ADDCONST): VmState {
    val stack = state.stack.value?.toMutableVmStack()

    return if (stack == null || stack.depth < 1) { // None or too few elements on stack
        VmStateException.of(state, VmStateException.STACK_UNDERFLOW)
    } else {
        val x = stack.pop() as? VmStackNumber

        if (x == null) { // Has to be a number
            VmStateException.of(state, VmStateException.TYPE_CHECK_ERROR)
        } else {
            val result = x + VmStackValue.of(instruction.cc)
            // TODO: Check for integer overflow
            stack.push(result)
            state.copy(stack = Maybe.of(stack))
        }
    }
}

internal fun executeMulConst(state: VmStateRunning, instruction: MULCONST): VmState {
    val stack = state.stack.value?.toMutableVmStack()

    return if (stack == null || stack.depth < 1) { // None or too few elements on stack
        VmStateException.of(state, VmStateException.STACK_UNDERFLOW)
    } else {
        val x = stack.pop() as? VmStackNumber

        if (x == null) { // Has to be a number
            VmStateException.of(state, VmStateException.TYPE_CHECK_ERROR)
        } else {
            val result = x + VmStackValue.of(instruction.cc)
            // TODO: Check for integer overflow
            stack.push(result)
            state.copy(stack = Maybe.of(stack))
        }
    }
}

internal fun executeMul(state: VmStateRunning, instruction: MUL): VmState {
    val stack = state.stack.value?.toMutableVmStack()

    return if (stack == null || stack.depth < 2) { // None or too few elements on stack
        VmStateException.of(state, VmStateException.STACK_UNDERFLOW)
    } else {
        val x = stack.pop() as? VmStackNumber
        val y = stack.pop() as? VmStackNumber

        if (x == null || y == null) { // Both need to be numbers
            VmStateException.of(state, VmStateException.TYPE_CHECK_ERROR)
        } else {
            val result = x * y
            // TODO: Check for integer overflow
            stack.push(result)
            state.copy(stack = Maybe.of(stack))
        }
    }
}
