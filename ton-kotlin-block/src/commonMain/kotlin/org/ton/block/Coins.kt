package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bigint.BigInt
import org.ton.bigint.times
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.providers.TlbConstructorProvider
import org.ton.tlb.storeTlb
import kotlin.math.pow

@SerialName("nanocoins")
@Serializable
data class Coins(
    val amount: VarUInteger = VarUInteger(0)
) {
    override fun toString() = toString(decimals = DECIMALS)

    fun toString(decimals: Int): String =
        amount.value.toString().let {
            it.dropLast(decimals).ifEmpty { "0" } + "." + it.takeLast(decimals).padStart(decimals, '0')
        }

    companion object : TlbConstructorProvider<Coins> by CoinsTlbConstructor {
        private val DECIMALS = 9

        @JvmStatic
        fun tlbCodec(): TlbCodec<Coins> = CoinsTlbConstructor

        @JvmStatic
        fun of(coins: Long, decimals: Int = DECIMALS): Coins =
            Coins(VarUInteger(BigInt(coins) * BigInt(10).pow(decimals)))

        @JvmStatic
        fun of(coins: Double, decimals: Int = DECIMALS): Coins =
            Coins(
                VarUInteger(
                    BigInt(coins.toLong() * 10.0.pow(decimals)) +
                        BigInt((coins - coins.toLong()) * 10.0.pow(decimals))
                )
            )

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
