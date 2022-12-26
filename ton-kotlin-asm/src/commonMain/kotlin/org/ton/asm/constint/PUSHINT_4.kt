package org.ton.asm.constint

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.storeUInt
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class PUSHINT_4(
    val i: UByte,
) : AsmInstruction {
    val x get(): UByte = (((i + 5u) and 15u) - 5u).toUByte()

    override fun toString(): String {
        return "$x PUSHINT"
    }

    companion object : TlbConstructorProvider<PUSHINT_4> by PUSHINT_4TlbConstructor
}

private object PUSHINT_4TlbConstructor : TlbConstructor<PUSHINT_4>(
    schema = "asm_pushint_4#7 i:uint4 = PUSHINT_4;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PUSHINT_4) {
        cellBuilder.storeUInt(value.i, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): PUSHINT_4 {
        val i = cellSlice.loadUInt(4).toUByte()
        return PUSHINT_4(i)
    }
}
