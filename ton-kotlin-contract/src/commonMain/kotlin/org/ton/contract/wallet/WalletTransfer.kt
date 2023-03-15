package org.ton.contract.wallet

import org.ton.block.*
import org.ton.cell.Cell
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

public data class WalletTransfer internal constructor(
    val destination: MsgAddressInt,
    val bounceable: Boolean,
    val coins: CurrencyCollection,
    val sendMode: Int,
    val body: Cell?,
    val stateInit: StateInit?
)

public class WalletTransferBuilder {
    public lateinit var destination: MsgAddressInt
    public var bounceable: Boolean = true
    public var currencyCollection: CurrencyCollection = CurrencyCollection(Coins(), ExtraCurrencyCollection())
    public var coins: Coins
        get() = currencyCollection.coins
        set(value) {
            currencyCollection = currencyCollection.copy(coins = value)
        }
    public var sendMode: Int = 3
    public var body: Cell? = null
    public var stateInit: StateInit? = null

    public fun build(): WalletTransfer =
        WalletTransfer(destination, bounceable, currencyCollection, sendMode, body, stateInit)
}

public inline fun WalletTransfer(builderAction: WalletTransferBuilder.() -> Unit): WalletTransfer {
    contract { callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE) }
    return WalletTransferBuilder().apply(builderAction).build()
}
