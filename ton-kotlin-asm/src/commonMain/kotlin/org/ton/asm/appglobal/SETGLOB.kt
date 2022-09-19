package org.ton.asm.appglobal

import org.ton.asm.AsmInstruction
import org.ton.bigint.*
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class SETGLOB(
    val k1: UByte,
) : AsmInstruction {
    init {
        require(k >= 1u) { "expected k >= 1, actual: $k" }
    }

    override fun toString(): String = "$k SETGLOB"

    companion object : TlbConstructorProvider<SETGLOB> by SETGLOBTlbConstructor
}

private object SETGLOBTlbConstructor : TlbConstructor<SETGLOB>(
    schema = "asm_setglob#f87_ k:(## 5) {1 <= k} = SETGLOB;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SETGLOB) {
        cellBuilder.storeUInt(value.k, 5)
    }

    override fun loadTlb(cellSlice: CellSlice): SETGLOB {
        val k = cellSlice.loadUInt(5).toUByte()
        return SETGLOB(k)
    }
}
