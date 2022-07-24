package org.ton.asm.constant.bitstring

import org.ton.asm.Instruction
import org.ton.block.VmCellSlice
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class PUSHSLICE(
    val slice: VmCellSlice
) : Instruction {
    override fun toString(): String = "$slice PUSHSLICE"

    companion object : TlbConstructorProvider<PUSHSLICE> by PUSHSLICETlbConstructor
}

// TODO: augment bytes in slice
private object PUSHSLICETlbConstructor : TlbConstructor<PUSHSLICE>(
    schema = "asm_pushslice#8b len:uint4 data:(bits (len * 8 + 4)) = PUSHSLICE;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PUSHSLICE) {
        TODO("Not yet implemented")
    }

    override fun loadTlb(cellSlice: CellSlice): PUSHSLICE {
        TODO("Not yet implemented")
    }
}