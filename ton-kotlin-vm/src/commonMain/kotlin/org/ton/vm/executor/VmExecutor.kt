package org.ton.vm.executor

import org.ton.asm.AsmInstruction
import org.ton.vm.VmState
import kotlin.reflect.KClass

class VmExecutor(
    var state: VmState
) {
    val instructions = HashMap<KClass<out AsmInstruction>, (VmState, AsmInstruction) -> VmState>().apply {
        registerStaticBasic()
        registerConstInt()
        registerArithmBasic()
    }

    fun execute(instruction: AsmInstruction) {
        state = instructions[instruction::class]!!.invoke(state, instruction)
    }
}

@Suppress("UNCHECKED_CAST")
internal inline fun <reified T : AsmInstruction> VmExecutor.register(noinline function: (VmState, T) -> VmState) {
    instructions[T::class] = function as (VmState, AsmInstruction) -> VmState
}