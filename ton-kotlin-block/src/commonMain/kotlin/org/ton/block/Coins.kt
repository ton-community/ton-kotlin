package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bigint.*
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.providers.TlbConstructorProvider
import org.ton.tlb.storeTlb

@SerialName("nanocoins")
@Serializable
data class Coins(
    val amount: VarUInteger = VarUInteger(0)
) {
    override fun toString(): String = buildString {
        val full = amount.value / NANOCOINS
        val decimal = amount.value - full
        append(full)
        append(".")
        append(decimal.toString().padStart(9, '0'))
    }

    companion object : TlbConstructorProvider<Coins> by CoinsTlbConstructor {
        private val NANOCOINS = 1_000_000_000

        @JvmStatic
        fun tlbCodec(): TlbCodec<Coins> = CoinsTlbConstructor

        @JvmStatic
        fun of(coins: Long): Coins = Coins(VarUInteger(BigInt(coins) * NANOCOINS))

        @JvmStatic
        fun of(coins: Double): Coins =
            Coins(VarUInteger(BigInt(coins.toLong() * NANOCOINS) + BigInt((coins - coins.toLong()) * NANOCOINS)))

        @JvmStatic
        fun ofNano(coins: Long): Coins = Coins(VarUInteger(coins))

        @JvmStatic
        fun ofNano(coins: BigInt): Coins = Coins(VarUInteger(coins))
    }
}

private object CoinsTlbConstructor : TlbConstructor<Coins>(
    schema = "nanocoins\$_ amount:(VarUInteger 16) = Coins;"
) {
    private val varUIntegerCodec = VarUInteger.tlbCodec(16)

    override fun storeTlb(
        cellBuilder: CellBuilder, value: Coins
    ) = cellBuilder {
        storeTlb(varUIntegerCodec, value.amount)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): Coins = cellSlice {
        val amount = loadTlb(varUIntegerCodec)
        Coins(amount)
    }
}
