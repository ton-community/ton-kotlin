package org.ton.adnl

import io.ktor.util.collections.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import org.ton.api.adnl.AdnlPing
import org.ton.api.adnl.AdnlPong
import org.ton.api.adnl.message.AdnlMessage
import org.ton.api.adnl.message.AdnlMessageAnswer
import org.ton.api.adnl.message.AdnlMessageQuery
import org.ton.bitstring.BitString
import org.ton.crypto.hex
import org.ton.crypto.sha256
import org.ton.logger.Logger
import org.ton.logger.PrintLnLogger
import org.ton.tl.TlCombinator
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.random.Random
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

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

    protected val supervisorJob = SupervisorJob()
    private val outputFlow = MutableSharedFlow<ByteArray>()
    private val inputFlow = MutableSharedFlow<ByteArray>()
    private val queryMap = ConcurrentMap<BitString, CompletableDeferred<AdnlMessageAnswer>>()
    private val pingMap = ConcurrentMap<AdnlPing, CompletableDeferred<AdnlPong>>()
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        logger.fatal {
            throwable.stackTraceToString()
        }
        runBlocking {
            disconnect()
        }
    }

    protected lateinit var input: ByteReadChannel
    protected lateinit var output: ByteWriteChannel

    abstract suspend fun connect(): AdnlTcpClient

    abstract suspend fun disconnect()

    suspend fun sendQuery(query: ByteArray): ByteArray = suspendCoroutine { continuation ->
        val queryId = nextQueryId()
        val adnlMessageQuery = AdnlMessageQuery(queryId.toByteArray(), query)
        val deferred = CompletableDeferred<AdnlMessageAnswer>()
        queryMap[queryId] = deferred

        val packet = AdnlMessageQuery.encodeBoxed(adnlMessageQuery)
        CoroutineScope(continuation.context).launch {
            outputFlow.emit(packet)
            val answer = deferred.await().answer
            continuation.resume(answer)
        }
    }

    @OptIn(ExperimentalTime::class)
    suspend fun ping(value: Long = Random.nextLong()) = measureTime {
        val ping = AdnlPing(value)
        val deferred = CompletableDeferred<AdnlPong>()
        pingMap[ping] = deferred

        val packet = AdnlPing.encodeBoxed(ping)
        outputFlow.emit(packet)

        deferred.await()
    }

    protected fun launchKeepAliveJob(delay: Duration) =
        CoroutineScope(dispatcher + supervisorJob + CoroutineName("ADNL Keep Alive")).launch {
            while (isActive) {
                ping()
                delay(delay)
            }
        }

    protected fun launchIoJob() {
        CoroutineScope(dispatcher + supervisorJob + exceptionHandler + CoroutineName("ADNL I/O INPUT")).launch {
            outputFlow.collect { outputPacket ->
                sendRaw(outputPacket)
            }
        }
        CoroutineScope(dispatcher + supervisorJob + exceptionHandler + CoroutineName("ADNL I/O OUTPUT")).launch {
            while (isActive) {
                val inputPacket = receiveRaw()
                inputFlow.emit(inputPacket)
            }
        }
        CoroutineScope(dispatcher + supervisorJob + exceptionHandler + CoroutineName("ADNL Packet Handler")).launch {
            inputFlow.collect { rawInput ->
                when (val adnlPacket = AdnlTlCombinator.decodeBoxed(rawInput)) {
                    is AdnlMessageAnswer -> {
                        val queryId = BitString(adnlPacket.queryId)
                        val deferred = requireNotNull(queryMap[queryId]) {
                            "Unexpected AdnlMessageAnswer with queryId: $queryId"
                        }
                        deferred.complete(adnlPacket)
                    }
                    is AdnlPong -> {
                        val value = AdnlPing(adnlPacket.value)
                        val deferred = requireNotNull(pingMap[value]) {
                            "Unexpected AdnlPong with value: $value"
                        }
                        deferred.complete(adnlPacket)
                    }
                    is AdnlPing -> {
                        val value = adnlPacket.value
                        val adnlPong = AdnlPong(value)
                        val rawOutput = AdnlPong.encodeBoxed(adnlPong)
                        outputFlow.emit(rawOutput)
                    }
                }
            }
        }
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

    protected suspend fun receiveRaw(): ByteArray {
        val length = input.readIntLittleEndian()

        check(length >= 64) { "Too small packet: $length" }
        check(length <= UShort.MAX_VALUE.toInt()) { "Too big packet: $length" }

        val nonce = input.readPacket(32).readBytes()
        val payload = input.readPacket(length - 64).readBytes()
        val hash = input.readPacket(32).readBytes()

        val actualHash = sha256(nonce, payload)
        check(hash.contentEquals(actualHash)) {
            "Invalid hash! expected: ${hex(hash)} actual: ${hex(actualHash)} payload: ${
                hex(
                    payload
                )
            }"
        }

        return payload
    }

    private fun nextQueryId(): BitString {
        var nextId: BitString
        while (true) {
            nextId = BitString(Random.nextBytes(256 / Byte.SIZE_BITS))
            if (!queryMap.containsKey(nextId)) break
        }
        return nextId
    }

    private object AdnlTlCombinator : TlCombinator<Any>(
        AdnlMessage.constructors + AdnlPing + AdnlPong
    )
}
