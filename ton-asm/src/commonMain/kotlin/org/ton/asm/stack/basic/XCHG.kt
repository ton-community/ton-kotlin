package org.ton.asm.stack.basic

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor

data class XCHG(
    val s1: Int,
    val s2: Int
) {
    companion object : TlbCodec<XCHG> by XCHGTlbCombinator {
        fun tlbCombinator(): TlbCombinator<XCHG> = XCHGTlbCombinator
    }
}

private object XCHGTlbCombinator : TlbCombinator<XCHG>() {
    override val constructors: List<TlbConstructor<out XCHG>> = listOf(
        XCHG10TlbConstructor, XCHG11TlbConstructor, XCHG1TlbConstructor
    )

    override fun getConstructor(value: XCHG): TlbConstructor<out XCHG> {
        return when (value.s1) {
            0 -> XCHG11TlbConstructor
            1 -> XCHG1TlbConstructor
            else -> XCHG10TlbConstructor
        }
    }
}

private object XCHG10TlbConstructor : TlbConstructor<XCHG>(
    schema = "asm_xchg_10#10 {n:#} {m:#} s1:uint4 s2:uint4 = XCHG n m;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: XCHG) {
        cellBuilder.storeUInt(value.s1, 4)
        cellBuilder.storeUInt(value.s2, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): XCHG {
        val s1 = cellSlice.loadUInt(4).toInt()
        val s2 = cellSlice.loadUInt(4).toInt()
        return XCHG(s1, s2)
    }
}

private object XCHG11TlbConstructor : TlbConstructor<XCHG>(
    schema = "asm_xchg_11#11 {n:#} {m:#} s2:uint8 = XCHG 0 m;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: XCHG) {
        cellBuilder.storeUInt(value.s2, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): XCHG {
        val s2 = cellSlice.loadUInt(8).toInt()
        return XCHG(0, s2)
    }
}

private object XCHG1TlbConstructor : TlbConstructor<XCHG>(
    schema = "asm_xchg_1#1 {n:#} {m:#} s2:uint4 = XCHG 1 m;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: XCHG) {
        cellBuilder.storeUInt(4, value.s2)
    }

    override fun loadTlb(cellSlice: CellSlice): XCHG {
        val s2 = cellSlice.loadUInt(4).toInt()
        return XCHG(1, s2)
    }
}