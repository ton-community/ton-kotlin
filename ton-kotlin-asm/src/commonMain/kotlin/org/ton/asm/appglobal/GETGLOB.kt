package org.ton.asm.appglobal

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class GETGLOB(
    val k: UByte,
) : AsmInstruction {
    init {
        require(k >= 1u) { "expected k >= 1, actual: $k" }
    }

    override fun toString(): String = "$k GETGLOB"

    companion object : TlbConstructorProvider<GETGLOB> by GETGLOBTlbConstructor
}

private object GETGLOBTlbConstructor : TlbConstructor<GETGLOB>(
    schema = "asm_getglob#f85_ k:(## 5) {1 <= k} = GETGLOB;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: GETGLOB) {
        cellBuilder.storeUInt(value.k, 5)
    }

    override fun loadTlb(cellSlice: CellSlice): GETGLOB {
        val k = cellSlice.loadUInt(5).toUByte()
        return GETGLOB(k)
    }
}
