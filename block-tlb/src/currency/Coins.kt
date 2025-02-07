package org.ton.kotlin.currency

import org.ton.kotlin.bigint.BigInt
import org.ton.kotlin.bigint.toBigInt
import org.ton.kotlin.cell.CellBuilder
import org.ton.kotlin.cell.CellContext
import org.ton.kotlin.cell.CellSlice
import org.ton.kotlin.cell.serialization.CellSerializer
import kotlin.jvm.JvmStatic

/**
 * Variable-length 120-bit integer. Used for native currencies.
 *
 * Stored as 4 bits of `len` (0..=15), followed by `len` bytes.
 *
 * @see [CurrencyCollection]
 */
public data class Coins(
    val amount: BigInt
) : Number(), Comparable<Coins> {
    init {
        require(amount.sign != -1) { "Amount must be greater than zero." }
    }

    public constructor(amount: Long) : this(amount.toBigInt())

    public fun toString(decimals: Int): String =
        amount.toString().let {
            it.dropLast(decimals).ifEmpty { "0" } + "." + it.takeLast(decimals).padStart(decimals, '0')
        }

    public operator fun plus(other: Coins): Coins = Coins(amount + other.amount)
    public operator fun minus(other: Coins): Coins = Coins(amount - other.amount)
    public operator fun times(other: Coins): Coins = Coins(amount * other.amount)
    public operator fun div(other: Coins): Coins = Coins(amount / other.amount)
    public operator fun rem(other: Coins): Coins = Coins(amount % other.amount)
    public operator fun inc(): Coins = Coins(amount.inc())
    public operator fun dec(): Coins = Coins(amount.dec())

    override fun toDouble(): Double = amount.toDouble()
    override fun toFloat(): Float = amount.toFloat()
    override fun toLong(): Long = amount.toLong()
    override fun toInt(): Int = amount.toInt()
    override fun toShort(): Short = amount.toShort()
    override fun toByte(): Byte = amount.toByte()
    override fun compareTo(other: Coins): Int = amount.compareTo(other.amount)

    public companion object : CellSerializer<Coins> by CoinsSerializer {
        public val ZERO: Coins = Coins(BigInt.ZERO)
        public val ONE: Coins = Coins(BigInt.ONE)
        public val TWO: Coins = Coins(BigInt.TWO)
        public val TEN: Coins = Coins(BigInt.TEN)
        public val MAX: Coins = Coins((BigInt.ONE shl 120) - BigInt.ONE)
        public val MIN: Coins = ZERO

        public const val TONCOIN_DECIMALS: Int = 8

        @JvmStatic
        public fun parse(input: CharSequence, decimals: Int): Coins {
            val parts = input.split('.')
            if (parts.size !in 1..2) {
                throw IllegalArgumentException("Invalid input")
            }
            val whole = BigInt(parts[0]) * BigInt.TEN.pow(decimals)
            if (parts.size == 1) {
                return Coins(whole)
            }
            if (parts[1].length > decimals) {
                throw IllegalArgumentException("too many decimals")
            }
            val decimal = StringBuilder(parts[1])
            while (decimal.length < decimals) {
                decimal.append('0')
            }
            val amount = whole + BigInt(decimal.toString())
            return Coins(amount)
        }
    }
}

private object CoinsSerializer : CellSerializer<Coins> {
    override fun load(slice: CellSlice, context: CellContext): Coins {
        val len = slice.loadUInt(4).toInt()
        val value = slice.loadBigInt(len * 8, signed = false)
        return Coins(value)
    }

    override fun store(
        builder: CellBuilder,
        value: Coins,
        context: CellContext
    ) {
        val len = (value.amount.bitLength + 7) ushr 3
        builder.storeUInt(len.toUInt(), 4)
        builder.storeBigInt(value.amount, len * 8, signed = false)
    }
}
