package org.ton.cell

import org.ton.boc.BagOfCells
import kotlin.test.Test
import kotlin.test.assertEquals

class BagOfCellsTest {
    @Test
    fun `simple BoC from bytes`() {
        val boc = BagOfCells("b5ee9c72010102010011000118000001010000000000000045010000".hexToByteArray())
        assertEquals(1, boc.roots.size)

        assertEquals("000001010000000000000045", boc.roots.first().bits.toByteArray().toHexString())
    }
}
