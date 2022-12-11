package org.ton.adnl.client

import kotlinx.coroutines.debug.junit4.CoroutinesTimeout
import org.junit.Rule

class ClientConnectionTest {
    @get:Rule
    val timeout = CoroutinesTimeout.seconds(15)

//    @Ignore
//    fun adnlSocketTest() = runBlocking {
//        val socket = aSocket(SelectorManager())
//            .tcp()
//            .connect(ipv4(908566172), 51565)
//            .adnl(Dispatchers.Default) {
//                serverPublicKey = PublicKeyEd25519(base64("TDg+ILLlRugRB4Kpg3wXjPcoc+d+Eeb7kuVe16CS9z8="))
//            }
//
//        val channel = socket.openWriteChannel()
//
//        val queryId = buildPacket {
//            writeLong(Clock.System.now().epochSeconds)
//            writeFully(Random.nextBytes(24))
//        }.readBytes()
//
//        channel.apply {
//            writePacket(testAdnlMessageQuery(queryId))
//            flush()
//        }
//        delay(1000)
//        val answer = socket.openReadChannel().readRemaining(48)
//        assertEquals(
//            0x1684ac0f,
//            answer.readInt()
//        ) // crc32("adnl.message.answer query_id:int256 answer:bytes = adnl.Message")
//        assertContentEquals(queryId, answer.readBytes(32)) // query_id:bytes
//        assertEquals(8, answer.readByte()) // answer:bytes length
//        assertEquals(0x0d0053e9, answer.readInt()) // crc32("liteServer.currentTime now:int = liteServer.CurrentTime")
//        val serverTime = Instant.fromEpochSeconds(answer.readIntLittleEndian().toLong())
//        println("server time: $serverTime")
//        assertContentEquals(ByteArray(3), answer.readBytes())
//    }
//
//    private fun testAdnlMessageQuery(queryId: ByteArray): ByteReadPacket {
//        val liteServerGetTime = buildPacket {
//            writeInt(0x345aad16) // crc32("liteServer.getTime = liteServer.CurrentTime")
//        }
//        assertContentEquals(
//            hex("345aad16"),
//            liteServerGetTime.copy().readBytes()
//        )
//
//        val liteServerQuery = buildPacket {
//            writeInt(0xdf068c79.toInt()) // crc32("liteServer.query data:bytes = Object")
//            writeByte(liteServerGetTime.remaining.toByte()) // data:bytes length
//            writePacket(liteServerGetTime) // data:bytes
//            repeat(4 - ceil(liteServerGetTime.remaining.toInt() + 1 / 4.0).toInt()) {
//                writeByte(0) // data:bytes padding
//            }
//        }
//        assertContentEquals(
//            hex("df068c7904345aad16000000"),
//            liteServerQuery.copy().readBytes()
//        )
//
//        val adnlMessageQuery = buildPacket {
//            writeInt(0x7af98bb4) // crc32("adnl.message.query query_id:int256 query:bytes = adnl.Message")
//            writeFully(queryId) // query_id:int256
//            writeByte(liteServerQuery.remaining.toByte()) // query:bytes length
//            writePacket(liteServerQuery)
//            repeat(4 - ceil(liteServerQuery.remaining.toInt() + 1 / 4.0).toInt()) {
//                writeByte(0) // query:bytes padding
//            }
//        }
//        assertContentEquals(
//            hex("7af98bb4") + queryId + hex("0cdf068c7904345aad16000000000000"),
//            adnlMessageQuery.copy().readBytes()
//        )
//        return adnlMessageQuery
//    }
}
