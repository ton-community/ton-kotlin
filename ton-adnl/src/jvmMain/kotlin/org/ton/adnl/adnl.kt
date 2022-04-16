package org.ton.adnl

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.util.collections.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import org.ton.adnl.message.AdnlMessageAnswer
import org.ton.adnl.message.AdnlMessageQuery
import org.ton.crypto.hex
import org.ton.crypto.sha256
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random

class AdnlClient(
    val host: String,
    val port: Int,
    val publicKey: AdnlPublicKey,
    private val dispatcher: CoroutineContext
) {
    private lateinit var job: Job
    private val sendFlow = MutableSharedFlow<AdnlMessageQuery>()
    private val queryMap = ConcurrentMap<String, CompletableDeferred<AdnlMessageAnswer>>()

    lateinit var connection: Connection
    lateinit var input: ByteReadChannel
    lateinit var output: ByteWriteChannel

    suspend fun connect() = apply {
        connection = aSocket(SelectorManager(dispatcher))
            .tcp()
            .connect(host, port)
            .connection()
        performHandshake()

        job = CoroutineScope(dispatcher).launch {
            while (isActive) {
                sendFlow.collect { adnlMessageQuery ->
                    val encoededPacket = AdnlMessageQuery.encodeBoxed(adnlMessageQuery)
                    sendRaw(encoededPacket)
                    val packet = receiveRaw()
                    when (packet.readIntLittleEndian()) {
                        AdnlMessageAnswer.id -> {
                            val adnlMessageAnswer = AdnlMessageAnswer.decode(packet)
                            val deferred = queryMap.remove(hex(adnlMessageAnswer.queryId))
                            deferred?.complete(adnlMessageAnswer)
                        }
                    }
                }
            }
        }
    }

    suspend fun sendQuery(query: ByteArray): ByteArray {
        val queryId = Random.nextBytes(32)
        val adnlMessageQuery = AdnlMessageQuery(queryId, query)
        val deferred = CompletableDeferred<AdnlMessageAnswer>()
        queryMap[hex(queryId)] = deferred

        sendFlow.emit(adnlMessageQuery)

        return deferred.await().answer
    }

    suspend fun sendRaw(packet: ByteArray, nonce: ByteArray = Random.nextBytes(32), flush: Boolean = true) {
        val length = packet.size + 64
        val hash = sha256(nonce, packet)
        val encryptedPacket = buildPacket {
            writeIntLittleEndian(length)
            writeFully(nonce)
            writeFully(packet)
            writeFully(hash)
        }
        output.writePacket(encryptedPacket)
        if (flush) {
            output.flush()
        }
    }

    private suspend fun receiveRaw(): ByteReadPacket {
        val length = input.readIntLittleEndian()

        check(length >= 64) { "Too small packet: $length" }
        check(length <= Short.MAX_VALUE) { "Too big packet: $length" }

        val nonce = input.readPacket(32).readBytes()
        val payload = input.readPacket(length - 64).readBytes()
        val hash = input.readPacket(32).readBytes()

        val actualHash = sha256(nonce, payload)
        check(hash.contentEquals(actualHash)) { "Invalid hash! expected: ${hex(hash)} actual: ${hex(actualHash)}" }

        return ByteReadPacket(payload)
    }

    private suspend fun performHandshake(
        clientPrivateKey: AdnlPrivateKey = AdnlPrivateKey.random(),
        aesParams: AdnlAesParams = AdnlAesParams.random(),
    ) {
        val clientPublicKey = clientPrivateKey.public()
        val sharedKey = clientPrivateKey.sharedKey(publicKey)
        val handshake =
            AdnlHandshake(
                publicKey.address(),
                clientPublicKey,
                aesParams,
                sharedKey
            ).build().readBytes()
        connection.output.writeFully(handshake)
        connection.output.flush()

        input = EncryptedByteReadChannel(connection.input, AdnlAes(aesParams.rxKey, aesParams.rxNonce))
        output = EncryptedByteWriteChannel(connection.output, AdnlAes(aesParams.txKey, aesParams.txNonce))

        check(receiveRaw().isEmpty) { "Invalid handshake response" }
    }
}
