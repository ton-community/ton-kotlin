package org.ton.asm.constint

import org.ton.asm.AsmInstruction
import org.ton.bigint.BigInt
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.bigint.*
import org.ton.tlb.providers.TlbConstructorProvider

data class PUSHINT_LONG(
    val l: UByte,
    val xxx: BigInt
) : AsmInstruction {
    override fun toString(): String = "$xxx PUSHINT"

    companion object : TlbConstructorProvider<PUSHINT_LONG> by PUSHINT_LONGTlbConstructor
}

private object PUSHINT_LONGTlbConstructor : TlbConstructor<PUSHINT_LONG>(
    schema = "asm_pushint_long#82 l:(## 5) xxx:(int (8 * l + 19)) = PUSHINT_LONG;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PUSHINT_LONG) {
        cellBuilder.storeUInt(value.l, 5)
        cellBuilder.storeInt(value.xxx, 8 * value.l.toInt() + 19)
    }

    override fun loadTlb(cellSlice: CellSlice): PUSHINT_LONG {
        val l = cellSlice.loadUInt(5).toUByte()
        val xxx = cellSlice.loadInt(8 * l.toInt() + 19)
        return PUSHINT_LONG(l, xxx)
    }
}
