package org.ton.asm.arithmdiv

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.storeUInt
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class MODPOW2(
    val tt: UByte
) : AsmInstruction {
    override fun toString(): String = "${tt + 1u} MODPOW2#"

    companion object : TlbConstructorProvider<MODPOW2> by MODPOW2TlbConstructor
}

private object MODPOW2TlbConstructor : TlbConstructor<MODPOW2>(
    schema = "asm_modpow2#a938 tt:uint8 = MODPOW2;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: MODPOW2) {
        cellBuilder.storeUInt(value.tt, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): MODPOW2 {
        val tt = cellSlice.loadUInt(8).toUByte()
        return MODPOW2(tt)
    }
}
