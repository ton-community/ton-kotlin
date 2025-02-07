package org.ton.block

import kotlinx.serialization.SerialName


@SerialName("action_reserve_currency")
public data class ActionReserveCurrency(
    val mode: Int,
    val currency: CurrencyCollection
) : OutAction
