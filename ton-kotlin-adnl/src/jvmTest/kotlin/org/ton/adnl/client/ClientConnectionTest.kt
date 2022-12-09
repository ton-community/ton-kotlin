package org.ton.adnl.client

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.debug.junit4.CoroutinesTimeout
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.junit.Rule
import org.ton.adnl.client.socket.adnl
import org.ton.adnl.ipv4
import org.ton.api.pub.PublicKeyEd25519
import org.ton.crypto.base64.base64
import org.ton.crypto.hex
import kotlin.math.ceil
import kotlin.random.Random
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class ClientConnectionTest {
    @get:Rule
    val timeout = CoroutinesTimeout.seconds(15)

    @Ignore
    fun adnlSocketTest() = runBlocking {
        val socket = aSocket(SelectorManager())
            .tcp()
            .connect(ipv4(908566172), 51565)
            .adnl(Dispatchers.Default) {
                serverPublicKey = PublicKeyEd25519(base64("TDg+ILLlRugRB4Kpg3wXjPcoc+d+Eeb7kuVe16CS9z8="))
            }

        val channel = socket.openWriteChannel()

        val queryId = buildPacket {
            writeLong(Clock.System.now().epochSeconds)
            writeFully(Random.nextBytes(24))
        }.readBytes()

        channel.apply {
            writePacket(testAdnlMessageQuery(queryId))
            flush()
        }
        delay(1000)
        val answer = socket.openReadChannel().readRemaining(48)
        assertEquals(
            0x1684ac0f,
            answer.readInt()
        ) // crc32("adnl.message.answer query_id:int256 answer:bytes = adnl.Message")
        assertContentEquals(queryId, answer.readBytes(32)) // query_id:bytes
        assertEquals(8, answer.readByte()) // answer:bytes length
        assertEquals(0x0d0053e9, answer.readInt()) // crc32("liteServer.currentTime now:int = liteServer.CurrentTime")
        val serverTime = Instant.fromEpochSeconds(answer.readIntLittleEndian().toLong())
        println("server time: $serverTime")
        assertContentEquals(ByteArray(3), answer.readBytes())
    }

    private fun testAdnlMessageQuery(queryId: ByteArray): ByteReadPacket {
        val liteServerGetTime = buildPacket {
            writeInt(0x345aad16) // crc32("liteServer.getTime = liteServer.CurrentTime")
        }
        assertContentEquals(
            hex("345aad16"),
            liteServerGetTime.copy().readBytes()
        )

        val liteServerQuery = buildPacket {
            writeInt(0xdf068c79.toInt()) // crc32("liteServer.query data:bytes = Object")
            writeByte(liteServerGetTime.remaining.toByte()) // data:bytes length
            writePacket(liteServerGetTime) // data:bytes
            repeat(4 - ceil(liteServerGetTime.remaining.toInt() + 1 / 4.0).toInt()) {
                writeByte(0) // data:bytes padding
            }
        }
        assertContentEquals(
            hex("df068c7904345aad16000000"),
            liteServerQuery.copy().readBytes()
        )

        val adnlMessageQuery = buildPacket {
            writeInt(0x7af98bb4) // crc32("adnl.message.query query_id:int256 query:bytes = adnl.Message")
            writeFully(queryId) // query_id:int256
            writeByte(liteServerQuery.remaining.toByte()) // query:bytes length
            writePacket(liteServerQuery)
            repeat(4 - ceil(liteServerQuery.remaining.toInt() + 1 / 4.0).toInt()) {
                writeByte(0) // query:bytes padding
            }
        }
        assertContentEquals(
            hex("7af98bb4") + queryId + hex("0cdf068c7904345aad16000000000000"),
            adnlMessageQuery.copy().readBytes()
        )
        return adnlMessageQuery
    }
}

// 7af98bb4 0000000062e81edacb4120ce51835cb6326d2d64855d1d73352ecfb07d7b46a9 0cdf068c7904345aad16000000000000
// 7af98bb4 15fb1dbe91f26a848f6c7ff0b516b417864d016b50ba0dfea5e44e52295d98f5 0cdf068c7904345aad16000000000000
// 7af98bb4 c7bd6a4532dec617b3d470e139762049a2fe3665fd81e57a3f9a8f2096f1cda6 0cdf068c7904345aad16000000000000
// 4c383e20b2e546e8110782a9837c178cf72873e77e11e6fb92e55ed7a092f73f
// 4c383e20b2e546e8110782a9837c178cf72873e77e11e6fb92e55ed7a092f73f
