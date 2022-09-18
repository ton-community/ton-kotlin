package org.ton.asm.arithmdiv

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.block.Either
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.providers.TlbConstructorProvider
import org.ton.tlb.storeTlb

data class DIV_BASE(
    val m: Boolean,
    val s: UByte,
    val cdft: Either<Pair<UByte, UByte>, Triple<UByte, UByte, UByte>>
) : AsmInstruction {
    constructor(
        m: Boolean,
        s: UByte,
        cdft: Pair<UByte, UByte>
    ) : this(m, s, Either.left(cdft))

    constructor(
        m: Boolean,
        s: UByte,
        cdft: Triple<UByte, UByte, UByte>
    ) : this(m, s, Either.right(cdft))

    override fun toString(): String = "$m $s ${cdft.x ?: cdft.y} DIV_BASE"

    companion object : TlbConstructorProvider<DIV_BASE> by DIV_BASETlbConstructor
}

private object DIV_BASETlbConstructor : TlbConstructor<DIV_BASE>(
    schema = "asm_div_base#a9 m:uint1 s:uint2 cdft:(Either [ d:uint2 f:uint2 ] [ d:uint2 f:uint2 tt:uint8 ]) = DIV_BASE;"
) {
    private val pair = object : TlbConstructor<Pair<UByte, UByte>>(
        schema = "_#_ [ d:uint2 f:uint2 ] = _;"
    ) {
        override fun storeTlb(cellBuilder: CellBuilder, value: Pair<UByte, UByte>) {
            cellBuilder.storeUInt(value.first, 2)
            cellBuilder.storeUInt(value.second, 2)
        }

        override fun loadTlb(cellSlice: CellSlice): Pair<UByte, UByte> {
            val d = cellSlice.loadUInt(2).toUByte()
            val f = cellSlice.loadUInt(2).toUByte()
            return d to f
        }
    }

    private val triple = object : TlbConstructor<Triple<UByte, UByte, UByte>>(
        schema = "_#_ [ d:uint2 f:uint2 tt:uint8 ] = _;"
    ) {
        override fun storeTlb(cellBuilder: CellBuilder, value: Triple<UByte, UByte, UByte>) {
            cellBuilder.storeUInt(value.first, 2)
            cellBuilder.storeUInt(value.second, 2)
            cellBuilder.storeUInt(value.third, 8)
        }

        override fun loadTlb(cellSlice: CellSlice): Triple<UByte, UByte, UByte> {
            val d = cellSlice.loadUInt(2).toUByte()
            val f = cellSlice.loadUInt(2).toUByte()
            val tt = cellSlice.loadUInt(8).toUByte()
            return Triple(d, f, tt)
        }
    }

    private val either = Either.tlbCodec(pair, triple)

    override fun storeTlb(cellBuilder: CellBuilder, value: DIV_BASE) {
        cellBuilder.storeBit(value.m)
        cellBuilder.storeUInt(value.s, 2)
        cellBuilder.storeTlb(either, value.cdft)
    }

    override fun loadTlb(cellSlice: CellSlice): DIV_BASE {
        val m = cellSlice.loadBit()
        val s = cellSlice.loadUInt(2).toUByte()
        val cdft = cellSlice.loadTlb(either)
        return DIV_BASE(m, s, cdft)
    }
}
