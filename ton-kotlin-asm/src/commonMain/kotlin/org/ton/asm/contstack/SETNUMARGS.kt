package org.ton.asm.contstack

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.bigint.*
import org.ton.tlb.providers.TlbConstructorProvider

data class SETNUMARGS(
    val n: UByte
) : AsmInstruction {
    init {
        require(n <= 14u) { "expected i <= 14, actual: $n" }
    }

    override fun toString(): String = "$n SETNUMARGS"

    companion object : TlbConstructorProvider<SETNUMARGS> by SETNUMARGSTlbConstructor
}

private object SETNUMARGSTlbConstructor : TlbConstructor<SETNUMARGS>(
    schema = "asm_setnumargs#ec0 n:(#<= 14) = SETNUMARGS;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SETNUMARGS) {
        cellBuilder.storeUIntLeq(value.n, 14)
    }

    override fun loadTlb(cellSlice: CellSlice): SETNUMARGS {
        val n = cellSlice.loadUIntLeq(14).toUByte()
        return SETNUMARGS(n)
    }
}
