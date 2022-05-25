package org.ton.adnl

import io.ktor.util.collections.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import org.ton.api.adnl.message.AdnlMessageAnswer
import org.ton.api.adnl.message.AdnlMessageQuery
import org.ton.crypto.hex
import org.ton.crypto.sha256
import org.ton.logger.Logger
import org.ton.logger.PrintLnLogger
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random

abstract class AdnlTcpClient(
    val host: String,
    val port: Int,
    val publicKey: AdnlPublicKey,
    protected val dispatcher: CoroutineContext,
    val logger: Logger = PrintLnLogger("TON ADNL")
) {
    constructor(
        ipv4: Int,
        port: Int,
        publicKey: AdnlPublicKey,
        dispatcher: CoroutineContext
    ) : this(ipv4(ipv4), port, publicKey, dispatcher)

    protected lateinit var job: Job
    private val sendFlow = MutableSharedFlow<AdnlMessageQuery>()
    private val queryMap = ConcurrentMap<String, CompletableDeferred<AdnlMessageAnswer>>()

    lateinit var input: ByteReadChannel
    lateinit var output: ByteWriteChannel

    abstract suspend fun connect(): AdnlTcpClient

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

    protected fun launchReceiveJob() = CoroutineScope(dispatcher).launch {
        while (isActive) {
            sendFlow.collect { adnlMessageQuery ->
                val encodedPacket = AdnlMessageQuery.encodeBoxed(adnlMessageQuery)
                sendRaw(encodedPacket)
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

    protected suspend fun receiveRaw(): ByteReadPacket {
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
}
