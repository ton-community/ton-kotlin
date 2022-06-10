package org.ton.block

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
        cellBuilder.storeTlb(VmStackList.tlbCodec(depth), vmStackList)
        val cell = cellBuilder.endCell()

        val cellSlice = cell.beginParse()
        val newVmStackList = cellSlice.loadTlb(VmStackList.tlbCodec(depth))

        assertEquals(vmStackList, newVmStackList)
    }
}
