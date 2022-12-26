package org.ton.asm.constdata

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.storeUInt
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class PUSHSLICE(
    val x: UByte,
    val sss: BitString
) : AsmInstruction {
    constructor(
        sss: BitString
    ) : this(
        x = ((sss.size - 4) / 8).toUByte(),
        sss = sss
    )

    fun toCellSlice(): CellSlice = CellSlice(sss)

    override fun toString(): String = "${toCellSlice()} PUSHSLICE"

    companion object : TlbConstructorProvider<PUSHSLICE> by PUSHSLICETlbConstructor
}

private object PUSHSLICETlbConstructor : TlbConstructor<PUSHSLICE>(
    schema = "asm_pushslice#8b x:(## 4) sss:((8 * x + 4) * Bit) = PUSHSLICE;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PUSHSLICE) {
        cellBuilder.storeUInt(value.x, 4)
        cellBuilder.storeBits(value.sss)
    }

    override fun loadTlb(cellSlice: CellSlice): PUSHSLICE {
        val x = cellSlice.loadUInt(4).toUByte()
        val sss = cellSlice.loadBits(8 * x.toInt() + 4)
        return PUSHSLICE(x, sss)
    }
}
