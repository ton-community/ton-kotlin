package org.ton.asm.cellbuild

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.bigint.*
import org.ton.bitstring.BitString
import org.ton.cell.Cell
import org.ton.tlb.providers.TlbConstructorProvider

data class STSLICECONST(
    val x: UByte,
    val y: UByte,
    val c: List<Cell>,
    val sss: BitString
) : AsmInstruction {
    constructor(
        c: List<Cell>,
        sss: BitString
    ) : this(
        x = c.size.toUByte(),
        y = ((sss.size - 2) / 8).toUByte(),
        c = c,
        sss = sss
    )

    init {
        require(c.size == x.toInt()) { "expected c.size: $x, actual: ${c.size}" }
        require(sss.size == 8 * y.toInt() + 2) { "expected sss.size: ${8u * y + 2u}, actual: ${sss.size}" }
    }

    fun toCellSlice(): CellSlice = CellSlice(sss, c)

    override fun toString(): String = "${toCellSlice()} STSLICECONST"

    companion object : TlbConstructorProvider<STSLICECONST> by STSLICECONSTTlbConstructor
}

private object STSLICECONSTTlbConstructor : TlbConstructor<STSLICECONST>(
    schema = "asm_stsliceconst#cfc0_ x:(## 2) y:(## 3) c:(x * ^Cell) sss:((8 * y + 2) * Bit) = STSLICECONST;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: STSLICECONST) {
        cellBuilder.storeUInt(value.x, 2)
        cellBuilder.storeUInt(value.y, 3)
        value.c.forEach { cell ->
            cellBuilder.storeRef(cell)
        }
        cellBuilder.storeBits(value.sss)
    }

    override fun loadTlb(cellSlice: CellSlice): STSLICECONST {
        val x = cellSlice.loadUInt(2).toUByte()
        val y = cellSlice.loadUInt(3).toUByte()
        val c = cellSlice.loadRefs(x.toInt())
        val sss = cellSlice.loadBits(8 * y.toInt() + 2)
        return STSLICECONST(x, y, c, sss)
    }
}
