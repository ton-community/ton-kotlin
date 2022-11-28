package org.ton.vm.instructions

import org.ton.asm.arithmbasic.ADD
import org.ton.bitstring.BitString
import org.ton.block.*
import org.ton.cell.CellSlice
import org.ton.hashmap.EmptyHashMapE
import org.ton.vm.VmStateRunning
import kotlin.test.Test
import kotlin.test.assertEquals

class arithmBasic {
    private val EMPTY_STATE = VmStateRunning(
        cp = 0,
        step = 0,
        gas = VmGasLimits(0, 0, 0, 0),
        stack = Maybe.of(null),
        save = VmSaveList(EmptyHashMapE()),
        code = VmCellSlice(CellSlice.of(BitString())),
        lib = VmLibraries(EmptyHashMapE())
    )

    @Test
    fun executeAdd() {
        val state = EMPTY_STATE.copy(
            stack = Maybe.of(
                MutableVmStack().apply {
                    push(VmStackValue.of(2))
                    push(VmStackValue.of(1))
                }
            ),
        )

        val result = executeAdd(state, ADD)

        assertEquals(true, result is VmStateRunning, "Successfully executed")
        assertEquals(1, (result as VmStateRunning).stack.value?.depth, "There is only one value left on the stack")
        assertEquals(3, (result.stack.value?.first() as VmStackNumber).toInt(), "The result is correct")
    }
}
