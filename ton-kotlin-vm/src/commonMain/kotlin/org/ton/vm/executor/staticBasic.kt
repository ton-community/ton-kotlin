package org.ton.vm.executor

import org.ton.asm.stackbasic.NOP
import org.ton.asm.stackbasic.SWAP
import org.ton.asm.stackbasic.XCHG_0I
import org.ton.vm.VmState

internal fun VmExecutor.registerStaticBasic() {
    register(::executeNop)
    register(::executeSwap)
    register(::executeXchg0i)
}

private fun executeNop(state: VmState, instruction: NOP) = state {
    // NOP.
}

private fun executeSwap(state: VmState, instruction: SWAP) = state {
    state.stack.swap()
}

private fun executeXchg0i(state: VmState, instruction: XCHG_0I) = state {
    state.stack.interchange(0u, instruction.i)
}
