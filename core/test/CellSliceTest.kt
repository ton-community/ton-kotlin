package org.ton.kotlin.cell

import kotlin.test.Test
import kotlin.test.assertEquals

class CellSliceTest {
    @Test
    fun `extract ints from slice`() {
        Cell("0000010100000000000000457C_").parse {
            assertEquals(0, loadBigInt(16, false).toInt())
            assertEquals(1, loadBigInt(8, false).toInt())
            assertEquals(1, loadBigInt(8, false).toInt())
            assertEquals(69, loadBigInt(64, false).toInt())
            assertEquals(15, loadBigInt(5, false).toInt())
        }
        Cell("0000FF880FFFFFFFFFFFFFFDDC_").parse {
            assertEquals(0, loadBigInt(16).toInt())
            assertEquals(-1, loadBigInt(8).toInt())
            assertEquals(-15, loadBigInt(5).toInt())
            assertEquals(1, loadBigInt(8).toInt())
            assertEquals(-69, loadBigInt(64).toInt())
        }
    }
}
