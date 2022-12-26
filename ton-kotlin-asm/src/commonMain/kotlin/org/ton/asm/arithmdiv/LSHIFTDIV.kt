package org.ton.asm.arithmdiv

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.storeUInt
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class LSHIFTDIV(
    val tt: UByte
) : AsmInstruction {
    override fun toString(): String = "${tt + 1u} LSHIFT#DIV"

    companion object : TlbConstructorProvider<LSHIFTDIV> by LSHIFTDIVTlbConstructor
}

private object LSHIFTDIVTlbConstructor : TlbConstructor<LSHIFTDIV>(
    schema = "asm_lshiftdiv#a9d4 tt:uint8 = LSHIFTDIV;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LSHIFTDIV) {
        cellBuilder.storeUInt(value.tt, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): LSHIFTDIV {
        val tt = cellSlice.loadUInt(8).toUByte()
        return LSHIFTDIV(tt)
    }
}
