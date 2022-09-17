package org.ton.asm.arithmdiv

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class LSHIFTDIVC(
    val tt: UByte
) : AsmInstruction {
    override fun toString(): String = "${tt+1u} LSHIFT#DIVC"

    companion object : TlbConstructorProvider<LSHIFTDIVC> by LSHIFTDIVCTlbConstructor
}

private object LSHIFTDIVCTlbConstructor : TlbConstructor<LSHIFTDIVC>(
    schema = "asm_lshiftdivc#a9d6 tt:uint8 = LSHIFTDIVC;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LSHIFTDIVC) {
        cellBuilder.storeUInt(value.tt, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): LSHIFTDIVC {
        val tt = cellSlice.loadUInt(8).toUByte()
        return LSHIFTDIVC(tt)
    }
}
