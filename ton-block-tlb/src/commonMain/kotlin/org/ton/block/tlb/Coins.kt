package org.ton.block.tlb

import org.ton.block.Coins
import org.ton.block.VarUInteger
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

fun Coins.Companion.tlbCodec(): TlbCodec<Coins> = CoinsTlbConstructor()

private class CoinsTlbConstructor : TlbConstructor<Coins>(
    schema = "nanocoins\$_ amount:(VarUInteger 16) = Coins;"
) {
    private val varUIntegerCodec = VarUInteger.tlbCodec(16)

    override fun encode(
        cellBuilder: CellBuilder, value: Coins, param: Int, negativeParam: (Int) -> Unit
    ) = cellBuilder {
        storeTlb(value.amount, varUIntegerCodec)
    }

    override fun decode(
        cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit
    ): Coins = cellSlice {
        val amount = loadTlb(varUIntegerCodec)
        Coins(amount)
    }
}
