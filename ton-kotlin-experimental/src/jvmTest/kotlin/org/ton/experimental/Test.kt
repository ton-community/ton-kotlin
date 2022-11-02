package org.ton.experimental

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.ton.api.adnl.AdnlAddressList
import org.ton.api.adnl.AdnlAddressUdp
import org.ton.api.adnl.AdnlPacketContents
import org.ton.api.dht.DhtNode
import org.ton.api.pub.PublicKeyEd25519
import org.ton.crypto.base64
import org.ton.crypto.hex
import org.ton.proxy.dht.Dht
import org.ton.proxy.dht.DhtPeer
import org.ton.proxy.dht.state.DhtState
import kotlin.test.Test

class Test {
    @Test
    fun test() = runBlocking {
        val dhtPeer = DhtPeer(
            DhtNode(
                id = PublicKeyEd25519(
                    base64("4R0C/zU56k+x2HGMsLWjX2rP/SpoTPIHSSAmidGlsb8=")
                ),
                addr_list = AdnlAddressList(
                    AdnlAddressUdp(
                        ip = -1952265919,
                        port = 14395
                    )
                )
            )
        )!!
        val dht = Dht.lite(listOf(dhtPeer))
        val addressList = dht.resolve(
            PublicKeyEd25519(base64("bn8klhFZgE2sfIDfvVI6m6+oVNi1nBRlnHoxKtR9WBU=")).toAdnlIdShort()
        )
        println("addressList: ${Json { prettyPrint = true }.encodeToString(addressList)}")
    }

    @Test
    fun packetContents() {
        val message =
            AdnlPacketContents.decodeBoxed(hex("89cd42d1073c306ea2a96d35d90c0000c6b413486ef4b8019c944ca848ace2f88a4f67dc08a8487d0ca11c9dd27a5659a185a2c6010000007af98bb400000000000000000000000000000000000000000000000000000000000000000c183febcb390500000000000000000001000000e7a60d670100007fce0c0000cde25f63cde25f6300000000b5e65f6300000000000000000000000000000000cde25f63ff1c74c840f45759eefb9c438532c6fe635f869290aee2b9324fbb46c569d05d5ee0f256df639030893be5977f01a2781d3c14bf9587d8129d8be5ad569cedf908b48b200100000007373585b0e7bb1b"))
        println(Json.encodeToString(message))
    }
}
