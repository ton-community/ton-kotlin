package org.ton.asm.arithmdiv

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class RSHIFTR(
    val tt: UByte
) : AsmInstruction {
    override fun toString(): String = "${tt + 1u} RSHIFTR#"

    companion object : TlbConstructorProvider<RSHIFTR> by RSHIFTRTlbConstructor
}

private object RSHIFTRTlbConstructor : TlbConstructor<RSHIFTR>(
    schema = "asm_rshiftr#a935 tt:uint8 = RSHIFTR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: RSHIFTR) {
        cellBuilder.storeUInt(value.tt, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): RSHIFTR {
        val tt = cellSlice.loadUInt(8).toUByte()
        return RSHIFTR(tt)
    }
}
