package org.ton.asm.appconfig

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class GETPARAM(
    val i: UByte
) : AsmInstruction {
    override fun toString(): String = "$i GETPARAM"

    companion object : TlbConstructorProvider<GETPARAM> by GETPARAMTlbConstructor
}

private object GETPARAMTlbConstructor : TlbConstructor<GETPARAM>(
    schema = "asm_getparam#f82 i:uint4 = GETPARAM;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: GETPARAM) {
        cellBuilder.storeUInt(value.i.toByte(), 4)
    }

    override fun loadTlb(cellSlice: CellSlice): GETPARAM {
        val i = cellSlice.loadTinyInt(4).toUByte()
        return GETPARAM(i)
    }
}
