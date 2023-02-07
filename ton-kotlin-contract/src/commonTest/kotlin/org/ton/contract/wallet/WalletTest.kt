package org.ton.contract.wallet

import kotlinx.coroutines.runBlocking
import org.ton.api.pk.PrivateKeyEd25519
import kotlin.test.Test

class WalletTest {
    @Test
    fun test(): Unit = runBlocking {
        val wallet = WalletV4R2Contract(PrivateKeyEd25519(ByteArray(32) { 0x00 }).publicKey())
        println(wallet.address.toString(
            userFriendly = true
        ))
    }
}
