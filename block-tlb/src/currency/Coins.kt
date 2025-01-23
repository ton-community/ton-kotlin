package org.ton.block.currency

import org.ton.bigint.BigInt
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbCodec
import kotlin.jvm.JvmStatic

/**
 * Variable-length 120-bit integer. Used for native currencies.
 *
 * Stored as 4 bits of `len` (0..=15), followed by `len` bytes.
 */
public data class Coins(
    val amount: BigInt
) {
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

    public object Tlb : TlbCodec<Coins> {
        override fun storeTlb(
            cellBuilder: CellBuilder, value: Coins
        ): Unit = cellBuilder {
            storeVarUInt(value.amount, 16)
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): Coins = cellSlice {
            val amount = loadVarUInt(16)
            Coins(amount)
        }
    }

    public companion object {
        public val ZERO: Coins = Coins(BigInt.ZERO)
        public val ONE: Coins = Coins(BigInt.ONE)
        public val TWO: Coins = Coins(BigInt.TWO)
        public val TEN: Coins = Coins(BigInt.TEN)
        public val MAX: Coins = Coins((BigInt.ONE shl (15 * 8)) - BigInt.ONE)
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

