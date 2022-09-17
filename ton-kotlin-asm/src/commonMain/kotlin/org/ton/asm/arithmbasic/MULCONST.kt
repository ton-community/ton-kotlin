package org.ton.asm.arithmbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class MULCONST(
    val cc: Byte
) : AsmInstruction {
    override fun toString(): String = "$cc MULCONST"

    companion object : TlbConstructorProvider<MULCONST> by MULCONSTTlbConstructor
}

private object MULCONSTTlbConstructor : TlbConstructor<MULCONST>(
    schema = "asm_mulconst#a7 cc:int8 = MULCONST;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: MULCONST) {
        cellBuilder.storeInt(value.cc, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): MULCONST {
        val cc = cellSlice.loadInt(8).toByte()
        return MULCONST(cc)
    }
}
