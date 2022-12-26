package org.ton.asm.contcreate

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.storeUInt
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class BLESSNUMARGS(
    val n: UByte
) : AsmInstruction {
    override fun toString(): String = "$n BLESSNUMARGS"

    companion object : TlbConstructorProvider<BLESSNUMARGS> by BLESSNUMARGSTlbConstructor
}

private object BLESSNUMARGSTlbConstructor : TlbConstructor<BLESSNUMARGS>(
    schema = "asm_blessnumargs#ee0 n:uint4 = BLESSNUMARGS;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: BLESSNUMARGS) {
        cellBuilder.storeUInt(value.n, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): BLESSNUMARGS {
        val n = cellSlice.loadUInt(4).toUByte()
        return BLESSNUMARGS(n)
    }
}
