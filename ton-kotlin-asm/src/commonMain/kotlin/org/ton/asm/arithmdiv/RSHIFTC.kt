package org.ton.asm.arithmdiv

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class RSHIFTC(
    val tt: UByte
) : AsmInstruction {
    override fun toString(): String = "${tt+1u} RSHIFTC#"

    companion object : TlbConstructorProvider<RSHIFTC> by RSHIFTCTlbConstructor
}

private object RSHIFTCTlbConstructor : TlbConstructor<RSHIFTC>(
    schema = "asm_rshiftc#a936 tt:uint8 = RSHIFTC;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: RSHIFTC) {
        cellBuilder.storeUInt(value.tt, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): RSHIFTC {
        val tt = cellSlice.loadUInt(8).toUByte()
        return RSHIFTC(tt)
    }
}
