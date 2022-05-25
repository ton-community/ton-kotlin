package org.ton.block.tlb

import org.ton.block.VmStackList
import org.ton.block.VmStackValue
import org.ton.cell.CellBuilder
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb
import kotlin.test.Test
import kotlin.test.assertEquals

class VmStackListTest {
    @Test
    fun `test VmStackList (de)serialization`() {
        val vmStackList = VmStackList(VmStackValue.TinyInt(37218))
        val depth = vmStackList.count()

        val cellBuilder = CellBuilder.beginCell()
        cellBuilder.storeTlb(vmStackList, VmStackList.tlbCodec(depth))
        val cell = cellBuilder.endCell()

        val cellSlice = cell.beginParse()
        val newVmStackList = cellSlice.loadTlb(VmStackList.tlbCodec(depth))

        assertEquals(vmStackList, newVmStackList)
    }
}
