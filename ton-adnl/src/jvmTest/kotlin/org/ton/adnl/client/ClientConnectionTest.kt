package org.ton.adnl.client

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.debug.junit4.CoroutinesTimeout
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.ton.adnl.ipv4
import org.ton.api.pub.PublicKeyEd25519
import org.ton.crypto.base64
import kotlin.random.Random

class ClientConnectionTest {
    @get:Rule
    val timeout = CoroutinesTimeout.seconds(15)

    @Test
    fun adnlWithoutCloseTest() = runBlocking {
        val selectorManager = ActorSelectorManager(Dispatchers.IO)
        val socket = aSocket(selectorManager)
            .tcp()
            .connect(ipv4(908566172), 51565)
            .adnl(Dispatchers.Default) {
                serverPublicKey = PublicKeyEd25519(base64("TDg+ILLlRugRB4Kpg3wXjPcoc+d+Eeb7kuVe16CS9z8="))
            }

        val channel = socket.openWriteChannel()

        buildPacket {
            writeIntLittleEndian(-1265895046) // constructor prefix: crc32("adnl.message.query query_id:int256 query:bytes = adnl.Message")
            writeFully(Random.nextBytes(32)) // query_id:int256
        }

        channel.apply {
            writePacket {

            }
            flush()
        }
    }
}
