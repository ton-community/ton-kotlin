package org.ton.block.action

import org.ton.block.currency.CurrencyCollection

public data class ActionReserveCurrency(
    val mode: Int,
    val currency: CurrencyCollection
) : OutAction
