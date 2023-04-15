package org.ton.adnl

import kotlinx.coroutines.runBlocking
import org.ton.api.pk.PrivateKeyEd25519
import org.ton.tl.ByteString.Companion.toByteString

class AdnlTest {
//    @Test
    fun test(): Unit = runBlocking {
        val adnl = Adnl.create(AdnlLoopbackNetworkEngine())

        val pk1 = PrivateKeyEd25519()
        val pub1 = pk1.publicKey()
        val src = pub1.toAdnlIdShort()

        val pk2 = PrivateKeyEd25519()
        val pub2 = pk2.publicKey()
        val dest = pub2.toAdnlIdShort()

        adnl.addLocalId(pk1)
        adnl.addLocalId(pk2)

        adnl.addPeer(src, pub2, AdnlLoopbackNetworkEngine.DUMMY_ADDRESS_LIST)

        adnl.subscribeMessage(dest) { src, dest, msg ->
            println("src=$src dest=$dest : $msg")
        }

        adnl.sendMessage(src, dest, "hello".encodeToByteArray().toByteString())
    }
}
