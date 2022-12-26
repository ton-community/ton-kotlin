package org.ton.asm.stackcomplex

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.storeUInt
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class XC2PU(
    val i: UByte,
    val j: UByte,
    val k: UByte
) : AsmInstruction {
    override fun toString(): String = "s$i s$j s$k XC2PU"

    companion object : TlbConstructorProvider<XC2PU> by XC2PUTlbConstructor
}

private object XC2PUTlbConstructor : TlbConstructor<XC2PU>(
    schema = "asm_xc2pu#541 i:uint4 j:uint4 k:uint4 = XC2PU;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: XC2PU) {
        cellBuilder.storeUInt(value.i, 4)
        cellBuilder.storeUInt(value.j, 4)
        cellBuilder.storeUInt(value.k, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): XC2PU {
        val i = cellSlice.loadUInt(4).toUByte()
        val j = cellSlice.loadUInt(4).toUByte()
        val k = cellSlice.loadUInt(4).toUByte()
        return XC2PU(i, j, k)
    }
}
