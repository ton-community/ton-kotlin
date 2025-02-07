package org.ton.kotlin.message.inmsg

import org.ton.kotlin.currency.Coins
import org.ton.kotlin.currency.CurrencyCollection

/**
 * Inbound message import fees.
 */
public data class ImportFees(
    /**
     * Fees collected from the message.
     */
    val feesCollected: Coins,

    /**
     * Value imported from the message.
     */
    val valueImported: CurrencyCollection
)