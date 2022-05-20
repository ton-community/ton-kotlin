package org.ton.cell

import org.ton.bitstring.BitString
import org.ton.crypto.hex
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class CellBuilderTest {
    @Test
    fun `build empty`() {
        var cell = CellBuilder.beginCell()
            .endCell()
        assertEquals(0, cell.bits.length)
        assertEquals(Cell(""), cell)
    }

    @Test
    fun `build single bit`() {
        var cell = CellBuilder.beginCell()
            .storeBit(true)
            .endCell()
        assertEquals(Cell(BitString.of(true)), cell)
    }

    @Test
    fun `build multiple bits`() {
        var cell = CellBuilder.beginCell()
            .storeBit(true)
            .storeBit(false)
            .storeBit(false)
            .storeBit(true)
            .storeBit(false)
            .endCell()
        assertEquals(Cell(BitString.of(true, false, false, true, false)), cell)
    }

    @Test
    fun `fail on too many bits added`() {
        var builder = CellBuilder.beginCell(10)
            .storeUInt(0, 10) // fine for now
        assertEquals(10, builder.bits.length)
        assertFails {
            builder.storeBit(false)
        }
    }

    @Test
    fun `build a number`() {
        var cell = CellBuilder.beginCell()
            .storeUInt(1, 8)
            .endCell()
        assertEquals(Cell("01"), cell)
    }

    @Test
    fun `build multiple numbers`() {
        var cell = CellBuilder.beginCell()
            .storeUInt(0, 16)
            .storeUInt(1, 8)
            .storeUInt(1, 8)
            .storeUInt(69, 64)
            .endCell()
        assertEquals("000001010000000000000045", hex(cell.bits.toByteArray()))
    }
}