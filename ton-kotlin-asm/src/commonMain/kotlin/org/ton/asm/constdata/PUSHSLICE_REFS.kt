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

data class PUSHSLICE_REFS(
    val r: UByte,
    val xx: UByte,
    val c: List<Cell>,
    val ssss: BitString
) : AsmInstruction {
    constructor(
        c: List<Cell>,
        ssss: BitString
    ) : this(
        r = (c.size - 1).toUByte(),
        xx = ((ssss.size - 1) / 8).toUByte(),
        c = c,
        ssss = ssss
    )

    init {
        require(c.size == r.toInt() + 1) { "expected c.size: ${r.toInt() + 1}, actual: ${c.size}" }
        require(ssss.size == 8 * xx.toInt() + 1) { "expected ssss.size: 8 * $xx + 1, actual: ${ssss.size}" }
    }

    fun toCellSlice(): CellSlice = CellSlice(ssss, c)

    override fun toString(): String = "${toCellSlice()} PUSHSLICE"

    companion object : TlbConstructorProvider<PUSHSLICE_REFS> by PUSHSLICE_REFSTlbConstructor
}

private object PUSHSLICE_REFSTlbConstructor : TlbConstructor<PUSHSLICE_REFS>(
    schema = "asm_pushslice_refs#8c r:(## 2) xx:(## 5) c:((r + 1) * ^Cell) ssss:((8 * xx + 1) * Bit) = PUSHSLICE_REFS;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PUSHSLICE_REFS) {
        cellBuilder.storeUInt(value.r, 2)
        cellBuilder.storeUInt(value.xx, 5)
        value.c.forEach { cell ->
            cellBuilder.storeRef(cell)
        }
        cellBuilder.storeBits(value.ssss)
    }

    override fun loadTlb(cellSlice: CellSlice): PUSHSLICE_REFS {
        val r = cellSlice.loadUInt(2).toUByte()
        val xx = cellSlice.loadUInt(5).toUByte()
        val c = cellSlice.loadRefs(r.toInt() + 1)
        val ssss = cellSlice.loadBits(8 * xx.toInt() + 1)
        return PUSHSLICE_REFS(r, xx, c, ssss)
    }
}
