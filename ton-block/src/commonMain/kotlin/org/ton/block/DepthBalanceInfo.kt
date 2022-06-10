package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("depth_balance")
data class DepthBalanceInfo(
    val split_depth: Int,
    val balance: CurrencyCollection
) {
    init {
        require(split_depth <= 30) { "required: split_depth <= 30, actual: $split_depth" }
    }
}