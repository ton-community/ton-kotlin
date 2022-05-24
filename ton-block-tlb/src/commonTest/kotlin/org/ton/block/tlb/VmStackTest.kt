package org.ton.block.tlb

import org.ton.block.VmStack
import org.ton.block.VmStackList
import org.ton.cell.CellBuilder
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb
import kotlin.test.Test
import kotlin.test.assertEquals

class VmStackTest {
    @Test
    fun `test VmStack (de)serialization`() {
        val stack = VmStack(0, VmStackList.Nil)
        val cell = CellBuilder.createCell {
            storeTlb(stack, VmStack.tlbCodec())
        }
        println(cell)
        val stack2 = cell.parse {
            loadTlb(VmStack.tlbCodec())
        }
        assertEquals(stack, stack2)
    }
}
