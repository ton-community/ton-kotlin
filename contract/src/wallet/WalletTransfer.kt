package org.ton.contract.wallet

import org.ton.api.pub.PublicKey
import org.ton.block.*
import org.ton.block.account.StateInit
import org.ton.block.currency.Coins
import org.ton.block.currency.CurrencyCollection
import org.ton.block.currency.ExtraCurrencyCollection
import org.ton.block.message.address.AddrInt
import org.ton.block.message.address.MsgAddress
import org.ton.cell.Cell
import org.ton.tlb.CellRef
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.jvm.JvmStatic

public data class WalletTransfer internal constructor(
    val destination: MsgAddress,
    val bounceable: Boolean,
    val coins: CurrencyCollection,
    val sendMode: Int,
    val messageData: MessageData
)

public class WalletTransferBuilder {
    public lateinit var destination: AddrInt
    public var bounceable: Boolean = true
    public var currencyCollection: CurrencyCollection = CurrencyCollection(Coins(), ExtraCurrencyCollection())
    public var coins: Coins
        get() = currencyCollection.coins
        set(value) {
            currencyCollection = currencyCollection.copy(coins = value)
        }
    public var sendMode: Int = 3
    public var messageData: MessageData = MessageData.Raw(Cell.empty(), null)

    public fun build(): WalletTransfer =
        WalletTransfer(destination, bounceable, currencyCollection, sendMode, messageData)
}

public inline fun WalletTransfer(builderAction: WalletTransferBuilder.() -> Unit): WalletTransfer {
    contract { callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE) }
    return WalletTransferBuilder().apply(builderAction).build()
}

public sealed interface MessageData {
    public val body: Cell
    public val stateInit: CellRef<StateInit>?

    public data class Raw(
        public override val body: Cell,
        public override val stateInit: CellRef<StateInit>?
    ) : MessageData

    public data class Text(
        public val text: CellRef<MessageText>
    ) : MessageData {
        public constructor(text: MessageText) : this(CellRef(text, MessageText))

        override val body: Cell get() = text.toCell(MessageText)
        override val stateInit: CellRef<StateInit>? get() = null
    }

    public companion object {
        @JvmStatic
        public fun raw(body: Cell, stateInit: CellRef<StateInit>? = null): Raw =
            Raw(body, stateInit)

        @JvmStatic
        public fun text(text: String): Text = Text(
            MessageText.Raw(text)
        )

        @JvmStatic
        public fun encryptedText(publicKey: PublicKey, text: String): Text = Text(
            MessageText.Raw(text).encrypt(publicKey)
        )
    }
}
