package org.ton.asm.arithmdiv

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.storeUInt
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class LSHIFTDIVR(
    val tt: UByte
) : AsmInstruction {
    override fun toString(): String = "${tt + 1u} LSHIFT#DIVR"

    companion object : TlbConstructorProvider<LSHIFTDIVR> by LSHIFTDIVRTlbConstructor
}

private object LSHIFTDIVRTlbConstructor : TlbConstructor<LSHIFTDIVR>(
    schema = "asm_lshiftdivr#a9d5 tt:uint8 = LSHIFTDIVR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: LSHIFTDIVR) {
        cellBuilder.storeUInt(value.tt, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): LSHIFTDIVR {
        val tt = cellSlice.loadUInt(8).toUByte()
        return LSHIFTDIVR(tt)
    }
}
