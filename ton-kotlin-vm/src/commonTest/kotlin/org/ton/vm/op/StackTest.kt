package org.ton.vm.op

import org.ton.asm.AsmInstruction
import org.ton.asm.stackbasic.*
import org.ton.cell.CellBuilder
import org.ton.vm.VirtualMachine
import org.ton.vm.VmStackValue
import org.ton.vm.VmStackValue.Companion.int
import org.ton.vm.stack.toMutableVmStack
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class StackTest {
    fun initVm(vararg stackValue: VmStackValue, builder: MutableList<AsmInstruction>.() -> Unit): VirtualMachine {
        val list = buildList(builder)
        val cellBuilder = CellBuilder()
        list.forEach {
            AsmInstruction.storeTlb(cellBuilder, it)
        }
        val codeCell = cellBuilder.endCell()
        println("Code: ${codeCell.beginParse()}")
        return VirtualMachine(codeCell.beginParse(), stackValue.toMutableVmStack())
    }

    @Test
    fun testNop() {
        val vm = initVm(int(1), int(2)) {
            add(NOP)
        }
        vm.run()
        assertEquals(2, vm.stack.depth)
    }

    @Test
    fun testSwap() {
        val vm = initVm(int(1), int(2)) {
            add(SWAP)
        }
        vm.run()
        assertEquals(1, vm.stack.popInt())
        assertEquals(2, vm.stack.popInt())
        assertTrue(vm.stack.isEmpty())
    }

    @Test
    fun testXch0i() {
        val vm = initVm(int(1), int(2), int(3), int(4)) {
            add(XCHG_0I(3))
        }
        vm.run()
        assertEquals(1, vm.stack.popInt())
        assertEquals(3, vm.stack.popInt())
        assertEquals(2, vm.stack.popInt())
        assertEquals(4, vm.stack.popInt())
        assertTrue(vm.stack.isEmpty())
    }

    @Test
    fun testXchij() {
        val vm = initVm(int(1), int(2), int(3), int(4)) {
            add(XCHG_IJ(1, 2))
        }
        vm.run()
        assertEquals(4, vm.stack.depth)
        assertEquals(1, vm.stack.getIntAt(0))
        assertEquals(3, vm.stack.getIntAt(1))
        assertEquals(2, vm.stack.getIntAt(2))
        assertEquals(4, vm.stack.getIntAt(3))
    }

    @Test
    fun testXchgii() {
        val vm = initVm(int(1), int(2), int(3), int(4)) {
            add(XCHG_0II(2))
        }
        vm.run()
        assertEquals(4, vm.stack.depth)
        assertEquals(3, vm.stack.getIntAt(0))
        assertEquals(2, vm.stack.getIntAt(1))
        assertEquals(1, vm.stack.getIntAt(2))
        assertEquals(4, vm.stack.getIntAt(3))
    }
}
