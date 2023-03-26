package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bigint.*
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.*
import org.ton.tlb.providers.TlbConstructorProvider
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic

@SerialName("nanocoins")
@Serializable
public data class Coins(
    @get:JvmName("amount")
    val amount: VarUInteger = VarUInteger(0)
) : TlbObject {
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer.type("nanocoins") {
        field("amount", amount)
    }

    override fun toString(): String = toString(decimals = DECIMALS)

    fun toString(decimals: Int): String =
        amount.value.toString().let {
            it.dropLast(decimals).ifEmpty { "0" } + "." + it.takeLast(decimals).padStart(decimals, '0')
        }

    operator fun plus(other: Coins): Coins = Coins(amount + other.amount)
    operator fun minus(other: Coins): Coins = Coins(amount - other.amount)
    operator fun times(other: Coins): Coins = Coins(amount * other.amount)
    operator fun div(other: Coins): Coins = Coins(amount / other.amount)
    operator fun rem(other: Coins): Coins = Coins(amount % other.amount)
    operator fun inc(): Coins = Coins(amount + VarUInteger(1, 1.toBigInt()))
    operator fun dec(): Coins = Coins(amount - VarUInteger(1, 1.toBigInt()))

    companion object : TlbConstructorProvider<Coins> by CoinsTlbConstructor {
        private val DECIMALS = 9

        @JvmStatic
        fun tlbCodec(): TlbCodec<Coins> = CoinsTlbConstructor

        @JvmStatic
        fun of(coins: Long, decimals: Int = DECIMALS): Coins =
            Coins(VarUInteger(coins.toBigInt() * 10.toBigInt().pow(decimals)))

        @JvmStatic
        fun of(coins: Double, decimals: Int = DECIMALS): Coins =
            Coins(
                VarUInteger(
                    (coins * 10.0.pow(decimals)).toLong().toBigInt()
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
