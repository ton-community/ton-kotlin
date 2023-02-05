package org.ton.adnl.connection

import io.ktor.utils.io.core.*
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withTimeout
import org.ton.api.adnl.message.AdnlMessageAnswer
import org.ton.api.adnl.message.AdnlMessageQuery
import org.ton.api.liteserver.LiteServerDesc
import org.ton.bitstring.Bits256
import kotlin.random.Random
import kotlin.time.Duration

public class AdnlClientImpl(
    private val liteServerDesc: LiteServerDesc
) : AdnlClient {
    override suspend fun sendQuery(data: ByteReadPacket, timeout: Duration): ByteReadPacket {
        val adnlConnection = connectionPool.selectConnection(liteServerDesc)
        val queryId = Bits256(Random.nextBytes(32))
        val context = SupervisorJob()
        val queryData = data.readBytes()
        try {
            return withTimeout(timeout) {
                val response = adnlConnection.execute(
                    AdnlRequestData(
                        buildPacket {
                            AdnlMessageQuery.encodeBoxed(
                                this, AdnlMessageQuery(queryId, queryData)
                            )
                        }.readBytes(),
                        context
                    ), context
                )
                ByteReadPacket(
                    AdnlMessageAnswer.decodeBoxed(
                        response.body
                    ).answer
                )
            }
        } catch (e: Throwable) {
            throw e
        }
    }

//    private suspend fun sendRaw(packet: ByteReadPacket) {
//        val dataSize = packet.remaining.toInt() + 32 + 32
//        require(dataSize in 32..(1 shl 24)) { "Invalid packet size: $dataSize" }
//
//        val nonce = SecureRandom.nextBytes(32)
//        val payload = packet.readBytes()
//        println("send payload: ${payload.encodeHex()}")
//        val hash = sha256(nonce, payload)
//        val data = buildPacket {
//            writeIntLittleEndian(dataSize)
//            writeFully(nonce)
//            writeFully(payload)
//            writeFully(hash)
//        }
//        val encryptedData = outputCipher.update(data.readBytes())
//        tcpClient.send(ByteReadPacket(encryptedData))
//    }
//
//    private suspend fun receiveRaw(): ByteReadPacket {
//        val encryptedLength = tcpClient.receive(4).readBytes()
//        val length = ByteReadPacket(inputCipher.update(encryptedLength)).readIntLittleEndian()
//        check(length in 32..(1 shl 24)) { "Invalid length" }
//        val encryptedData = tcpClient.receive(length).readBytes()
//        val data = ByteReadPacket(inputCipher.update(encryptedData))
//        val payload = data.readBytes((data.remaining - 32).toInt())
//        val hash = data.readBytes(32)
//
//        require(sha256(payload).contentEquals(hash)) {
//            "sha256 mismatch"
//        }
//
//        val packet = ByteReadPacket(payload)
//        packet.discard(32) // nonce
//
//        return packet
//    }
//
//    private suspend fun handshake() {
//        val id = remoteKey.toAdnlIdShort().id
//        val nonce = SecureRandom.nextBytes(160) // TODO: return random
//        initCrypto(nonce)
//
//        val data = buildPacket {
//            writeFully(id.toByteArray())
//            writeFully(remoteKey.encrypt(nonce))
//        }
//
//        tcpClient.send(data)
//        check(receiveRaw().remaining == 0L) { "Handshake failed" }
//    }
//
//    private fun initCrypto(nonce: ByteArray) {
//        require(nonce.size >= 96) { "too small crypto nonce, expected: 96, actual: ${nonce.size}" }
//        val s1 = nonce.copyOfRange(0, 32)
//        val s2 = nonce.copyOfRange(32, 64)
//        val v1 = nonce.copyOfRange(64, 80)
//        val v2 = nonce.copyOfRange(80, 96)
//        inputCipher = AesCtr(s1, v1)
//        outputCipher = AesCtr(s2, v2)
//    }
//
//    companion object {
//        private val adnlDispatcher = newFixedThreadPoolContext(2, "adnl")
//    }

    public companion object {
        private val connectionPool: AdnlConnectionPool = AdnlConnectionPool()
    }
}
