package org.ton.kotlin.examples

import org.ton.api.pk.PrivateKeyEd25519
import org.ton.block.AddrStd
import org.ton.block.Coins
import org.ton.block.CurrencyCollection
import org.ton.block.balance
import org.ton.contract.wallet.WalletTransfer
import org.ton.kotlin.currency.VarUInt248
import org.ton.kotlin.examples.contract.WalletV1R3Contract
import org.ton.kotlin.examples.faucet.TestnetFaucet
import org.ton.kotlin.examples.provider.LiteClientProvider
import org.ton.kotlin.examples.provider.liteClientTestnet

val swapAddress = AddrStd("kQC_rkxBuZDwS81yvMSLzeXBNLCGFNofm0avwlMfNXCwoOgr")

suspend fun main() {
    val provider = LiteClientProvider(liteClientTestnet())

    val key = PrivateKeyEd25519()
    val wallet = WalletV1R3Contract(0, key.publicKey(), provider)
    var state = wallet.getState()
    val faucet = TestnetFaucet(provider)

    faucet.topUpContract(wallet.address, 4000000000)
    state = requireNotNull(wallet.waitForStateChange(state))
    println("new balance: ${state.balance}")

    println("requesting from $swapAddress ECHIDNA...")
    wallet.transfer(key, wallet.getSeqno(state), listOf(WalletTransfer {
        destination = swapAddress
        coins = Coins(3700000000)
    }))

    while (true) {
        state = requireNotNull(wallet.waitForStateChange(state))
        println("new balance: ${state.balance}")
        if (!state.balance.other.isEmpty()) {
            break
        }
    }

    println("new extra-currency balance ${state.balance.other[100]} ECHIDNA")

    println("send back to $swapAddress")
    wallet.transfer(key, wallet.getSeqno(state), listOf(WalletTransfer {
        destination = swapAddress
        currencyCollection = CurrencyCollection(Coins(100000000), mapOf(100 to VarUInt248(600000000)))
    }))

    state = requireNotNull(wallet.waitForStateChange(state))
    println("new balance: ${state.balance}")
}

