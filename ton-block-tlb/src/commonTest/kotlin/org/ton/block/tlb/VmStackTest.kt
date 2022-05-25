package org.ton.block.tlb

import org.ton.block.VmStack
import org.ton.block.VmStackList
import org.ton.block.VmStackValue
import org.ton.cell.CellBuilder
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb
import kotlin.test.Test
import kotlin.test.assertEquals

class VmStackTest {
    @Test
    fun `test VmStack (de)serialization with VmStackList-Nil`() {
        val stack = VmStack(VmStackList.Nil)
        val cell = CellBuilder.createCell {
            storeTlb(VmStack.tlbCodec(), stack)
        }
        println(cell)
        val stack2 = cell.parse {
            loadTlb(VmStack.tlbCodec())
        }
        assertEquals(stack, stack2)
    }

    @Test
    fun `test VmStack (de)serialization with VmStackList-Single`() {
        val stack = VmStack(
            VmStackList.Cons(
                VmStackList.Cons(
                    VmStackList.Nil, VmStackValue.TinyInt(1313)
                ), VmStackValue.Int(12)
            )
        )
        val cell = CellBuilder.createCell {
            storeTlb(VmStack.tlbCodec(), stack)
        }
        println(cell)
        val stack2 = cell.parse {
            loadTlb(VmStack.tlbCodec())
        }
        assertEquals(stack, stack2)
    }
}
