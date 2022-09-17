package org.ton.asm.contdict

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUShort
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class PREPAREDICT(
    val n: UShort
) : AsmInstruction {
    override fun toString(): String = "$n PREPAREDICT"

    companion object : TlbConstructorProvider<PREPAREDICT> by PREPAREDICTTlbConstructor
}

private object PREPAREDICTTlbConstructor : TlbConstructor<PREPAREDICT>(
    schema = "asm_preparedict#f1a_ n:uint14 = PREPAREDICT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PREPAREDICT) {
        cellBuilder.storeUInt(value.n, 14)
    }

    override fun loadTlb(cellSlice: CellSlice): PREPAREDICT {
        val n = cellSlice.loadUInt(14).toUShort()
        return PREPAREDICT(n)
    }
}
