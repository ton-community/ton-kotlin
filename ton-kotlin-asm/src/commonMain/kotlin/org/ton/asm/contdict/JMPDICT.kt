package org.ton.asm.contdict

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUShort
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.storeUInt
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class JMPDICT(
    val n: UShort
) : AsmInstruction {
    override fun toString(): String = "$n JMP"

    companion object : TlbConstructorProvider<JMPDICT> by JMPDICTTlbConstructor
}

private object JMPDICTTlbConstructor : TlbConstructor<JMPDICT>(
    schema = "asm_jmpdict#f16_ n:uint14 = JMPDICT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: JMPDICT) {
        cellBuilder.storeUInt(value.n, 14)
    }

    override fun loadTlb(cellSlice: CellSlice): JMPDICT {
        val n = cellSlice.loadUInt(14).toUShort()
        return JMPDICT(n)
    }
}
