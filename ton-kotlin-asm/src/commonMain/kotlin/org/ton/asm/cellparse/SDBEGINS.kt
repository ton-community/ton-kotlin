package org.ton.asm.cellparse

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.storeUInt
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class SDBEGINS(
    val x: UByte,
    val sss: BitString
) : AsmInstruction {
    constructor(
        sss: BitString
    ) : this(
        x = ((sss.size - 3) / 8).toUByte(),
        sss = sss
    )

    init {
        require(sss.size == 8 * x.toInt() + 6) { "expected sss.size: ${8u * x + 3u}, actual: ${sss.size}" }
    }

    fun toCellSlice(): CellSlice = CellSlice(sss)

    override fun toString(): String = "${toCellSlice()} SDBEGINS"

    companion object : TlbConstructorProvider<SDBEGINS> by SDBEGINSTlbConstructor
}

private object SDBEGINSTlbConstructor : TlbConstructor<SDBEGINS>(
    schema = "asm_sdbegins#d72a_ x:(## 7) sss:((8 * x + 3) * Bit) = SDBEGINS;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SDBEGINS) {
        cellBuilder.storeUInt(value.x, 7)
        cellBuilder.storeBits(value.sss)
    }

    override fun loadTlb(cellSlice: CellSlice): SDBEGINS {
        val x = cellSlice.loadUInt(7).toUByte()
        val sss = cellSlice.loadBits(8 * x.toInt() + 3)
        return SDBEGINS(x, sss)
    }
}
