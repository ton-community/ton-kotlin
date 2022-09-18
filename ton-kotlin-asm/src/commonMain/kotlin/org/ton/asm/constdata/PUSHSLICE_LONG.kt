package org.ton.asm.constdata

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.bitstring.BitString
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class PUSHSLICE_LONG(
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
        xx = ((ssss.size - 6) / 8).toUByte(),
        c = c,
        ssss = ssss
    )

    init {
        require(r <= 4u) { "expected r <= 4, actual: $r" }
        require(c.size == r.toInt()) { "expected c.size: $r, actual: ${c.size}" }
        require(ssss.size == 8 * xx.toInt() + 6) { "expected ssss.size: 8 * $xx + 6, actual: ${ssss.size}" }
    }

    fun toCellSlice(): CellSlice = CellSlice(ssss, c)

    override fun toString(): String = "${toCellSlice()} PUSHSLICE"

    companion object : TlbConstructorProvider<PUSHSLICE_LONG> by PUSHSLICE_LONGTlbConstructor
}

private object PUSHSLICE_LONGTlbConstructor : TlbConstructor<PUSHSLICE_LONG>(
    schema = "asm_pushslice_long#8d r:(#<= 4) xx:(## 7) c:(r * ^Cell) ssss:((8 * xx + 6) * Bit) = PUSHSLICE_LONG;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PUSHSLICE_LONG) {
        cellBuilder.storeUIntLeq(value.r, 4)
        cellBuilder.storeUInt(value.xx, 7)
        value.c.forEach { cell ->
            cellBuilder.storeRef(cell)
        }
        cellBuilder.storeBits(value.ssss)
    }

    override fun loadTlb(cellSlice: CellSlice): PUSHSLICE_LONG {
        val r = cellSlice.loadUIntLeq(4).toUByte()
        val xx = cellSlice.loadUInt(7).toUByte()
        val c = cellSlice.loadRefs(r.toInt())
        val ssss = cellSlice.loadBits(8 * xx.toInt() + 6)
        return PUSHSLICE_LONG(r, xx, c, ssss)
    }
}
