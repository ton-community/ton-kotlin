package org.ton.asm.constdata

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.bitstring.BitString
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.storeUInt
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class PUSHCONT(
    val r: UByte,
    val xx: UByte,
    val c: List<Cell>,
    val ssss: BitString
) : AsmInstruction {
    constructor(
        c: List<Cell>,
        ssss: BitString
    ) : this(
        r = c.size.toUByte(),
        xx = (ssss.size / 8).toUByte(),
        c = c,
        ssss = ssss
    )

    fun toCellBuilder(): CellBuilder = CellBuilder(Cell(ssss, c))
    override fun toString(): String = "${toCellBuilder()} PUSHCONT"

    companion object : TlbConstructorProvider<PUSHCONT> by PUSHCONTTlbConstructor
}

private object PUSHCONTTlbConstructor : TlbConstructor<PUSHCONT>(
    schema = "asm_pushrefcont#8f_ r:(## 2) xx:(## 7) c:(r * ^Cell) ssss:((8 * xx) * Bit) = PUSHCONT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PUSHCONT) {
        cellBuilder.storeUInt(value.r, 2)
        cellBuilder.storeUInt(value.xx, 7)
        value.c.forEach { cell ->
            cellBuilder.storeRef(cell)
        }
        cellBuilder.storeBits(value.ssss)
    }

    override fun loadTlb(cellSlice: CellSlice): PUSHCONT {
        val r = cellSlice.loadUInt(2).toUByte()
        val xx = cellSlice.loadUInt(7).toUByte()
        val c = cellSlice.loadRefs(r.toInt())
        val ssss = cellSlice.loadBits(8 * xx.toInt())
        return PUSHCONT(r, xx, c, ssss)
    }
}
