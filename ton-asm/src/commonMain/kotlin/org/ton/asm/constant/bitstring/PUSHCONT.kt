package org.ton.asm.constant.bitstring

import org.ton.asm.Instruction
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbCombinatorProvider

data class PUSHCONT(
    val code: Cell
) : Instruction {
    fun instructions(): List<Instruction> = Instruction.loadList(code.beginParse())

    override fun toString(): String = "<{\n${instructions().joinToString("\n")}\n}> PUSHCONT"

    companion object : TlbCombinatorProvider<PUSHCONT> by PUSHCONTTlbCombinator
}

private object PUSHCONTTlbCombinator : TlbCombinator<PUSHCONT>() {
    override val constructors: List<TlbConstructor<out PUSHCONT>> = listOf(
        PUSHCONTSmallTlbConstructor, PUSHCONTFullTlbConstructor
    )

    override fun getConstructor(value: PUSHCONT): TlbConstructor<out PUSHCONT> {
        return if (value.code.refs.isEmpty()) {
            PUSHCONTSmallTlbConstructor
        } else {
            PUSHCONTFullTlbConstructor
        }
    }
}

private object PUSHCONTSmallTlbConstructor : TlbConstructor<PUSHCONT>(
    schema = "asm_pushcont_small#9 len:uint4 code:(bits (len * 8)) = PUSHCONT;",
    type = PUSHCONT::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PUSHCONT) {
        val len = value.code.bits.size / 8
        cellBuilder.storeUInt(len, 4)
        cellBuilder.storeBits(value.code.bits)
    }

    override fun loadTlb(cellSlice: CellSlice): PUSHCONT {
        val len = cellSlice.loadUInt(4).toInt() * 8
        val code = Cell(cellSlice.loadBits(len))
        return PUSHCONT(code)
    }
}

private object PUSHCONTFullTlbConstructor : TlbConstructor<PUSHCONT>(
    schema = "asm_pushcont_full#8f_ refs:uint2 len:uint7 data:(bits (len * 8)) = PUSHCONT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PUSHCONT) {
        cellBuilder.storeUInt(value.code.refs.size - 1, 2)
        cellBuilder.storeUInt(value.code.bits.size / 8, 7)
        cellBuilder.storeBits(value.code.bits)
        value.code.refs.forEach { ref ->
            cellBuilder.storeRef(ref)
        }
    }

    override fun loadTlb(cellSlice: CellSlice): PUSHCONT {
        val refs = cellSlice.loadUInt(2).toInt() + 1
        val len = cellSlice.loadUInt(7).toInt() * 8
        val code = Cell(
            cellSlice.loadBits(len),
            cellSlice.loadRefs(refs)
        )
        return PUSHCONT(code)
    }
}