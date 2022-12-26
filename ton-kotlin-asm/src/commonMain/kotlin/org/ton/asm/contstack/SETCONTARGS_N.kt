package org.ton.asm.contstack

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.storeUInt
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class SETCONTARGS_N(
    val r: UByte,
    val n: UByte
) : AsmInstruction {
    init {
        require(n <= 14u) { "expected r <= 14, actual: $n" }
    }

    override fun toString(): String = "$r $n SETCONTARGS"

    companion object : TlbConstructorProvider<SETCONTARGS_N> by SETCONTARGS_NTlbConstructor
}

private object SETCONTARGS_NTlbConstructor : TlbConstructor<SETCONTARGS_N>(
    schema = "asm_setcontargs_n#ec r:uint4 n:(#<= 14) = SETCONTARGS_N;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SETCONTARGS_N) {
        cellBuilder.storeUInt(value.r, 4)
        cellBuilder.storeUIntLeq(value.n.toInt(), 14)
    }

    override fun loadTlb(cellSlice: CellSlice): SETCONTARGS_N {
        val r = cellSlice.loadUInt(4).toUByte()
        val n = cellSlice.loadUIntLeq(14).toUByte()
        return SETCONTARGS_N(r, n)
    }
}
