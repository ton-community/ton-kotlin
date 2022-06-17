package org.ton.block

import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@Serializable
data class ShardFeeCreated(
    val fees: CurrencyCollection,
    val create: CurrencyCollection
) {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<ShardFeeCreated> = ShardFeeCreatedTlbConstructor
    }
}

private object ShardFeeCreatedTlbConstructor : TlbConstructor<ShardFeeCreated>(
    schema = "_ fees:CurrencyCollection create:CurrencyCollection = ShardFeeCreated;\n"
) {
    val currencyCollection by lazy { CurrencyCollection.tlbCodec() }

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: ShardFeeCreated
    ) = cellBuilder {
        storeTlb(currencyCollection, value.fees)
        storeTlb(currencyCollection, value.create)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): ShardFeeCreated = cellSlice {
        val fees = loadTlb(currencyCollection)
        val create = loadTlb(currencyCollection)
        ShardFeeCreated(fees, create)
    }
}