package org.ton.asm.arithmbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class ADDCONST(
    val cc: Byte
) : AsmInstruction {
    override fun toString(): String = "$cc ADDCONST"

    companion object : TlbConstructorProvider<ADDCONST> by ADDCONSTTlbConstructor
}

private object ADDCONSTTlbConstructor : TlbConstructor<ADDCONST>(
    schema = "asm_addconst#a6 cc:int8 = ADDCONST;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: ADDCONST) {
        cellBuilder.storeInt(value.cc, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): ADDCONST {
        val cc = cellSlice.loadInt(8).toByte()
        return ADDCONST(cc)
    }
}
