package org.ton.vm.instructions

import org.ton.asm.constint.*
import org.ton.block.Maybe
import org.ton.block.MutableVmStack
import org.ton.block.VmStackNan
import org.ton.block.VmStackValue
import org.ton.vm.VmState
import org.ton.vm.VmStateRunning

internal fun executePushInt4(state: VmStateRunning, instruction: PUSHINT_4): VmState =
    state.copy(
        stack = Maybe.of(
            (state.stack.value?.toMutableVmStack() ?: MutableVmStack()).apply {
                push(VmStackValue.of(instruction.x.toInt()))
            })
    )

private fun executePushInt8(state: VmStateRunning, instruction: PUSHINT_8): VmState =
    state.copy(
        stack = Maybe.of(
            (state.stack.value?.toMutableVmStack() ?: MutableVmStack()).apply {
                push(VmStackValue.of(instruction.xx))
            })
    )

private fun executePushInt16(state: VmStateRunning, instruction: PUSHINT_16): VmState =
    state.copy(
        stack = Maybe.of(
            (state.stack.value?.toMutableVmStack() ?: MutableVmStack()).apply {
                push(VmStackValue.of(instruction.xxxx))
            })
    )

private fun executePushIntLong(state: VmStateRunning, instruction: PUSHINT_LONG): VmState =
    state.copy(
        stack = Maybe.of(
            (state.stack.value?.toMutableVmStack() ?: MutableVmStack()).apply {
                push(VmStackValue.of(instruction.xxx))
            })
    )

private fun executePushNan(state: VmStateRunning, instruction: PUSHNAN): VmState =
    state.copy(
        stack = Maybe.of(
            (state.stack.value?.toMutableVmStack() ?: MutableVmStack()).apply {
                push(VmStackNan)
            })
    )
