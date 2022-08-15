package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.providers.TlbConstructorProvider
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

    companion object : TlbConstructorProvider<DepthBalanceInfo> by DepthBalanceInfoTlbConstructor
}

private object DepthBalanceInfoTlbConstructor : TlbConstructor<DepthBalanceInfo>(
    schema = "depth_balance\$_ split_depth:(#<= 30) balance:CurrencyCollection = DepthBalanceInfo;"
) {

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: DepthBalanceInfo
    ) = cellBuilder {
        storeUIntLeq(value.split_depth, 30)
        storeTlb(CurrencyCollection, value.balance)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): DepthBalanceInfo = cellSlice {
        val splitDepth = loadUIntLeq(30).toInt()
        val balance = loadTlb(CurrencyCollection)
        DepthBalanceInfo(splitDepth, balance)
    }
}
