package org.ton.asm.stackcomplex

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.storeUInt
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class PUSH_LONG(
    val ii: UByte
) : AsmInstruction {
    override fun toString(): String = "$ii s() PUSH"

    companion object : TlbConstructorProvider<PUSH_LONG> by PUSH_LONGTlbConstructor
}

private object PUSH_LONGTlbConstructor : TlbConstructor<PUSH_LONG>(
    schema = "asm_push_long#56 ii:uint8 = PUSH_LONG;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PUSH_LONG) {
        cellBuilder.storeUInt(value.ii, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): PUSH_LONG {
        val ii = cellSlice.loadUInt(8).toUByte()
        return PUSH_LONG(ii)
    }
}
