package org.ton.asm.constant.integer

import org.ton.asm.Instruction
import org.ton.bigint.BigInt
import org.ton.bigint.bitLength
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbCombinatorProvider

data class PUSHINT(
    val i: BigInt
) : Instruction {
    constructor(i: Int) : this(BigInt(i))
    constructor(i: Long) : this(BigInt(i))

    override fun toString(): String = "$i PUSHINT"

    companion object : TlbCombinatorProvider<PUSHINT> by PUSHINTTlbCombinator
}

private object PUSHINTTlbCombinator : TlbCombinator<PUSHINT>() {
    override val constructors: List<TlbConstructor<out PUSHINT>> = listOf(
        PUSHINT4TlbConstructor, PUSHINT8TlbConstructor, PUSHINT16TlbConstructor, PUSHINTTlbConstructor
    )

    override fun getConstructor(value: PUSHINT): TlbConstructor<out PUSHINT> {
        val bitLength = value.i.bitLength
        return when {
            bitLength <= 4 -> PUSHINT4TlbConstructor
            bitLength <= 8 -> PUSHINT8TlbConstructor
            bitLength <= 16 -> PUSHINT16TlbConstructor
            else -> PUSHINTTlbConstructor
        }
    }
}

private object PUSHINT4TlbConstructor : TlbConstructor<PUSHINT>(
    schema = "asm_pushint4#7 i:int4 = PUSHINT;",
    type = PUSHINT::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PUSHINT) {
        cellBuilder.storeInt(value.i, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): PUSHINT {
        val i = cellSlice.loadInt(4)
        return PUSHINT(i)
    }
}

private object PUSHINT8TlbConstructor : TlbConstructor<PUSHINT>(
    schema = "asm_pushint8#80 i:int8 = PUSHINT;",
    type = PUSHINT::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PUSHINT) {
        cellBuilder.storeInt(value.i, 8)
    }

    override fun loadTlb(cellSlice: CellSlice): PUSHINT {
        val i = cellSlice.loadInt(8)
        return PUSHINT(i)
    }
}

private object PUSHINT16TlbConstructor : TlbConstructor<PUSHINT>(
    schema = "asm_pushint16#81 i:int16 = PUSHINT;",
    type = PUSHINT::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PUSHINT) {
        cellBuilder.storeInt(value.i, 16)
    }

    override fun loadTlb(cellSlice: CellSlice): PUSHINT {
        val i = cellSlice.loadInt(16)
        return PUSHINT(i)
    }
}

private object PUSHINTTlbConstructor : TlbConstructor<PUSHINT>(
    schema = "asm_pushint#82 len:uint5 i:(int (19 + len * 8)) = PUSHINT;",
    type = PUSHINT::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PUSHINT) {
        val len = value.i.bitLength / 8
        cellBuilder.storeUInt(len, 5)
        cellBuilder.storeInt(value.i, 19 + len * 8)
    }

    override fun loadTlb(cellSlice: CellSlice): PUSHINT {
        val len = cellSlice.loadTinyInt(5).toInt()
        val i = cellSlice.loadInt(19 + len * 8)
        return PUSHINT(i)
    }
}