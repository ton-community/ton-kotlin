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

data class PUSHCONT_SHORT(
    val x: UByte,
    val ssss: BitString
) : AsmInstruction {

    fun toCellBuilder(): CellBuilder = CellBuilder(Cell(ssss))
    override fun toString(): String = "${toCellBuilder()} PUSHCONT"

    companion object : TlbConstructorProvider<PUSHCONT_SHORT> by PUSHCONT_SHORTTlbConstructor
}

private object PUSHCONT_SHORTTlbConstructor : TlbConstructor<PUSHCONT_SHORT>(
    schema = "asm_pushcont_short#9 x:(## 4) ssss:((8 * x) * Bit) = PUSHCONT_SHORT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PUSHCONT_SHORT) {
        cellBuilder.storeUInt(value.x, 4)
        cellBuilder.storeBits(value.ssss)
    }

    override fun loadTlb(cellSlice: CellSlice): PUSHCONT_SHORT {
        val x = cellSlice.loadUInt(4).toUByte()
        val ssss = cellSlice.loadBits(8 * x.toInt())
        return PUSHCONT_SHORT(x, ssss)
    }
}
