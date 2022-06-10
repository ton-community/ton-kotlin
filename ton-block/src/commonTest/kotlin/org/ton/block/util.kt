package org.ton.block

import org.ton.cell.CellBuilder
import org.ton.tlb.TlbCodec
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb
import kotlin.test.assertEquals

fun <T : Any> testSerialization(codec: TlbCodec<T>, stackValue: T) {
    val cellBuilder = CellBuilder.beginCell()
    cellBuilder.storeTlb(codec, stackValue)
    val cell = cellBuilder.endCell()

    val cellSlice = cell.beginParse()
    val result = cellSlice.loadTlb(codec)
    assertEquals(stackValue, result)
    cellSlice.endParse()
}
