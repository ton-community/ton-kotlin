package org.ton.asm.codepage

import org.ton.asm.stack.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbCombinatorProvider

data class SETCP(
    val cp: Int
) : Instruction {
    override fun toString(): String = "$cp SETCP"

    companion object : TlbCombinatorProvider<SETCP> by SETCPTlbCombinator
}

private object SETCPTlbCombinator : TlbCombinator<SETCP>() {
    override val constructors: List<TlbConstructor<out SETCP>> = listOf(SETCPFFTlbConstructor, SETCPFFFTlbConstructor)

    override fun getConstructor(value: SETCP): TlbConstructor<out SETCP> {
        return if (value.cp >= 0) SETCPFFTlbConstructor
        else SETCPFFFTlbConstructor
    }
}

private object SETCPFFTlbConstructor : TlbConstructor<SETCP>(
    schema = "asm_setcp_ff#ff cp:uint8 = SETCP;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SETCP) {
        cellBuilder.storeUInt(value.cp, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): SETCP {
        val cp = cellSlice.loadUInt(8).toInt()
        return SETCP(cp)
    }
}

private object SETCPFFFTlbConstructor : TlbConstructor<SETCP>(
    schema = "asm_setcp_fff#fff cp:uint4 = SETCP;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SETCP) {
        cellBuilder.storeUInt(value.cp + 16, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): SETCP {
        val cp = cellSlice.loadUInt(4).toInt() - 16
        return SETCP(cp)
    }
}
