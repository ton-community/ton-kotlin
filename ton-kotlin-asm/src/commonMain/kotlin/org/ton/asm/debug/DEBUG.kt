package org.ton.asm.debug

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class DEBUG(
    val nn: UByte,
) : AsmInstruction {

    override fun toString(): String = "$nn DEBUG"

    companion object : TlbConstructorProvider<DEBUG> by DEBUGTlbConstructor
}

private object DEBUGTlbConstructor : TlbConstructor<DEBUG>(
    schema = "asm_debug#fe nn:(#<= 239) = DEBUG;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DEBUG) {
        cellBuilder.storeUIntLeq(value.nn.toByte(), 239.toByte())
    }

    override fun loadTlb(cellSlice: CellSlice): DEBUG {
        val nn = cellSlice.loadUIntLeq(239).toUByte()
        return DEBUG(nn)
    }
}
