package org.ton.cell

import kotlin.test.Test
import kotlin.test.assertEquals

class CellSliceTest {
    @Test
    fun `extract ints from slice`() {
        var cs = Cell("000001010000000000000045").beginParse()
        assertEquals(0, cs.loadUInt(16).toInt())
        assertEquals(1, cs.loadUInt(8).toInt())
        assertEquals(1, cs.loadUInt(8).toInt())
        assertEquals(69, cs.loadUInt(64).toInt())
    }
}