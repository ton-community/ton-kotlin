package org.ton.asm.codepage

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

public data class SETCP(
    val nn: Int,
) : AsmInstruction {
    override fun toString(): String = "$nn SETCP"

    public companion object : TlbConstructorProvider<SETCP> by SETCPTlbConstructor
}

private object SETCPTlbConstructor : TlbConstructor<SETCP>(
    schema = "asm_setcp#ff nn:(#<= 239) = SETCP;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SETCP) {
        cellBuilder.storeUIntLeq(value.nn, 239)
    }

    override fun loadTlb(cellSlice: CellSlice): SETCP {
        val nn = cellSlice.loadUInt(239).toInt()
        return SETCP(nn)
    }
}
