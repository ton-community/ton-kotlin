package org.ton.asm.stackcomplex

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.storeUInt
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class POP_LONG(
    val ii: UByte
) : AsmInstruction {
    override fun toString(): String = "$ii s() POP"

    companion object : TlbConstructorProvider<POP_LONG> by POP_LONGTlbConstructor
}

private object POP_LONGTlbConstructor : TlbConstructor<POP_LONG>(
    schema = "asm_pop_long#57 ii:uint8 = POP_LONG;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: POP_LONG) {
        cellBuilder.storeUInt(value.ii, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): POP_LONG {
        val ii = cellSlice.loadUInt(8).toUByte()
        return POP_LONG(ii)
    }
}
