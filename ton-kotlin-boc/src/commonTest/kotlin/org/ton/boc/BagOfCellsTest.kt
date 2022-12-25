package org.ton.boc

import io.ktor.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class BagOfCellsTest {
    @Test
    fun `simple BoC from bytes`() {
        val boc = BagOfCells(hex("b5ee9c72010102010011000118000001010000000000000045010000"))
        assertEquals(1, boc.roots.size)

        assertEquals("000001010000000000000045", hex(boc.roots.first().bits.toByteArray()))
    }
}
