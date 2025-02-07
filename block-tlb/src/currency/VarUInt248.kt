package org.ton.kotlin.currency

import org.ton.kotlin.bigint.BigInt
import org.ton.kotlin.bigint.toBigInt
import org.ton.kotlin.cell.CellBuilder
import org.ton.kotlin.cell.CellContext
import org.ton.kotlin.cell.CellSlice
import org.ton.kotlin.cell.serialization.CellSerializer

/**
 * Variable-length 248-bit integer. Used for extra currencies.
 *
 * Stored as 5 bits of `len` (0..=31), followed by `len` bytes.
 *
 * @see [ExtraCurrencyCollection]
 */
public data class VarUInt248(
    public val amount: BigInt
) : Number(), Comparable<VarUInt248> {
    init {
        require(amount.sign != -1) { "Amount must be greater than zero." }
    }

    public constructor(amount: Long) : this(amount.toBigInt())

    override fun toDouble(): Double = amount.toDouble()
    override fun toFloat(): Float = amount.toFloat()
    override fun toLong(): Long = amount.toLong()
    override fun toInt(): Int = amount.toInt()
    override fun toShort(): Short = amount.toShort()
    override fun toByte(): Byte = amount.toByte()

    override fun compareTo(other: VarUInt248): Int {
        return amount.compareTo(other.amount)
    }

    public companion object : CellSerializer<VarUInt248> by VarUInt248Serializer {
        public val ZERO: VarUInt248 = VarUInt248(BigInt.ZERO)
        public val ONE: VarUInt248 = VarUInt248(BigInt.ONE)
        public val TWO: VarUInt248 = VarUInt248(BigInt.TWO)
        public val TEN: VarUInt248 = VarUInt248(BigInt.TEN)
        public val MAX: VarUInt248 = VarUInt248((BigInt.ONE shl 248) - BigInt.ONE)
        public val MIN: VarUInt248 = ZERO
    }
}

private object VarUInt248Serializer : CellSerializer<VarUInt248> {
    override fun load(slice: CellSlice, context: CellContext): VarUInt248 {
        val len = slice.loadUInt(5).toInt()
        val value = slice.loadBigInt(len * 8, signed = false)
        return VarUInt248(value)
    }

    override fun store(
        builder: CellBuilder,
        value: VarUInt248,
        context: CellContext
    ) {
        val len = (value.amount.bitLength + 7) ushr 3
        builder.storeUInt(len.toUInt(), 5)
        builder.storeBigInt(value.amount, len * 8, signed = false)
    }
}