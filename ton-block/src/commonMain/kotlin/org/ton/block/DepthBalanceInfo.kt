package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@Serializable
@SerialName("depth_balance")
data class DepthBalanceInfo(
    val split_depth: Int,
    val balance: CurrencyCollection
) {
    init {
        require(split_depth <= 30) { "required: split_depth <= 30, actual: $split_depth" }
    }

    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<DepthBalanceInfo> = DepthBalanceInfoTlbConstructor
    }
}

private object DepthBalanceInfoTlbConstructor : TlbConstructor<DepthBalanceInfo>(
    schema = "depth_balance\$_ split_depth:(#<= 30) balance:CurrencyCollection = DepthBalanceInfo;"
) {
    val currencyCollection by lazy { CurrencyCollection.tlbCodec() }

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: DepthBalanceInfo
    ) = cellBuilder {
        storeUIntLeq(value.split_depth, 30)
        storeTlb(currencyCollection, value.balance)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): DepthBalanceInfo = cellSlice {
        val splitDepth = loadUIntLeq(30).toInt()
        val balance = loadTlb(currencyCollection)
        DepthBalanceInfo(splitDepth, balance)
    }
}