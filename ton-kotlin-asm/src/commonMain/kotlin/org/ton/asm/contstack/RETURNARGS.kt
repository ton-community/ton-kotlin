package org.ton.asm.contstack

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.storeUInt
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class RETURNARGS(
    val p: UByte
) : AsmInstruction {
    override fun toString(): String = "$p RETURNARGS"

    companion object : TlbConstructorProvider<RETURNARGS> by RETURNARGSTlbConstructor
}

private object RETURNARGSTlbConstructor : TlbConstructor<RETURNARGS>(
    schema = "asm_returnargs#ed0 p:uint4 = RETURNARGS;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: RETURNARGS) {
        cellBuilder.storeUInt(value.p, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): RETURNARGS {
        val p = cellSlice.loadUInt(4).toUByte()
        return RETURNARGS(p)
    }
}
