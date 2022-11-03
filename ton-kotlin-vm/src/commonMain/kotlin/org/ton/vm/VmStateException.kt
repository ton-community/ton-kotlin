package org.ton.vm

import org.ton.block.VmGasLimits
import org.ton.block.VmLibraries
import org.ton.block.VmSaveList
import org.ton.block.VmStackValue

data class VmStateException(
    val cp: Short,
    val step: Int,
    val gas: VmGasLimits,
    val exc_no: Int,
    val exc_arg: VmStackValue,
    val save: VmSaveList,
    val lib: VmLibraries,
) : VmState {
    override fun step(): VmState {
        TODO("Not yet implemented")
    }

    companion object {
        const val NORMAL_TERMINATION = 0
        const val ALTERNATIVE_TERMINATION = 1
        const val STACK_UNDERFLOW = 2
        const val STACK_OVERFLOW = 3
        const val INTEGER_OVERFLOW = 4
        const val RANGE_CHECK_ERROR = 5
        const val INVALID_OPCODE = 6
        const val TYPE_CHECK_ERROR = 7
        const val CELL_OVERFLOW = 8
        const val CELL_UNDERFLOW = 9
        const val DICTIONARY_ERROR = 10
        const val UNKNOWN_ERROR = 11
        const val FATAL_ERROR = 12
        const val OUT_OF_GAS = 13

        @JvmStatic
        fun of(state: VmStateRunning, exc_no: Int, exc_arg: VmStackValue = VmStackValue.of(0)): VmStateException {
            return VmStateException(
                state.cp,
                state.step,
                state.gas,
                exc_no,
                exc_arg,
                state.save,
                state.lib
            )
        }
    }
}
