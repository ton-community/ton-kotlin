package org.ton.asm.contbasic

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class JMPXARGS(
    val p: UByte
) : AsmInstruction {
    override fun toString(): String = "$p JMPXARGS"

    companion object : TlbConstructorProvider<JMPXARGS> by JMPXARGSTlbConstructor
}

private object JMPXARGSTlbConstructor : TlbConstructor<JMPXARGS>(
    schema = "asm_jmpxargs#db1 p:uint4 = JMPXARGS;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: JMPXARGS) {
        cellBuilder.storeUInt(value.p, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): JMPXARGS {
        val p = cellSlice.loadUInt(4).toUByte()
        return JMPXARGS(p)
    }
}
