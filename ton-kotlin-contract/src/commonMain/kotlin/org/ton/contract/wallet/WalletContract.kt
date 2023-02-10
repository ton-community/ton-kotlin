package org.ton.contract.wallet

import org.ton.api.pub.PublicKeyEd25519
import org.ton.bitstring.Bits256
import org.ton.block.*
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.contract.SmartContract
import org.ton.lite.api.LiteApi
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.jvm.JvmField

public interface WalletContract : SmartContract {

    public suspend fun getSeqno(liteApi: LiteApi): Int {
        val stack = runGetMethod(liteApi, "seqno").let {
            check(it.isSuccess && it.stack != null) { "seqno failed with exit code ${it.exitCode}" }
            it.stack
        }
        return stack.toMutableVmStack().popInt().toInt()
    }

    public suspend fun getPublicKey(liteApi: LiteApi): PublicKeyEd25519 {
        val stack = runGetMethod(liteApi, "get_public_key").let {
            check(it.isSuccess && it.stack != null) { "get_public_key failed with exit code ${it.exitCode}" }
            it.stack
        }
        val int = stack.toMutableVmStack().popInt()
        val key = CellBuilder.createCell {
            storeUInt(int, 256)
        }.bits

        return PublicKeyEd25519(Bits256(key))
    }

    public companion object {
        @JvmField
        public val DEFAULT_WALLET_ID: Int = 698983191
    }
}

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

@OptIn(ExperimentalContracts::class)
public inline fun WalletTransfer(builderAction: WalletTransferBuilder.() -> Unit): WalletTransfer {
    contract { callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE) }
    return WalletTransferBuilder().apply(builderAction).build()
}
