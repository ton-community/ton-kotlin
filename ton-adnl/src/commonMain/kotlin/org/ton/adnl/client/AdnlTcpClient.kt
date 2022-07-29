package org.ton.adnl.client

import io.ktor.util.collections.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.locks.reentrantLock
import kotlinx.atomicfu.locks.withLock
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import org.ton.adnl.ipv4
import org.ton.api.adnl.AdnlPing
import org.ton.api.adnl.AdnlPong
import org.ton.api.adnl.message.AdnlMessage
import org.ton.api.adnl.message.AdnlMessageAnswer
import org.ton.api.adnl.message.AdnlMessageQuery
import org.ton.api.pub.PublicKey
import org.ton.bitstring.BitString
import org.ton.crypto.SecureRandom
import org.ton.crypto.encodeHex
import org.ton.crypto.hex
import org.ton.crypto.sha256
import org.ton.logger.Logger
import org.ton.logger.PrintLnLogger
import org.ton.tl.TlCombinator
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.random.Random
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

abstract class AdnlTcpClient(
    val host: String,
    val port: Int,
    val publicKey: PublicKey,
    protected val dispatcher: CoroutineContext = Dispatchers.IO,
    val logger: Logger = PrintLnLogger("TON ADNL")
) {
    constructor(
        ipv4: Int,
        port: Int,
        publicKey: PublicKey,
        dispatcher: CoroutineContext = Dispatchers.IO
    ) : this(ipv4(ipv4), port, publicKey, dispatcher)

    protected lateinit var supervisorJob: CompletableJob
    private val outputFlow = MutableSharedFlow<ByteArray>()
    private val inputFlow = MutableSharedFlow<ByteArray>()
    private val queryLock = reentrantLock()
    protected val queryMap = ConcurrentMap<BitString, CompletableDeferred<AdnlMessageAnswer>>()
    private val pingMap = ConcurrentMap<AdnlPing, CompletableDeferred<AdnlPong>>()
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        logger.fatal {
            throwable.toString()
        }
        runBlocking {
            disconnect()
        }
    }
    private var receivedPacketCount by atomic(0L)
    private var sentPacketCount by atomic(0L)

    protected lateinit var input: ByteReadChannel
    protected lateinit var output: ByteWriteChannel

    abstract suspend fun connect(): AdnlTcpClient

    abstract suspend fun disconnect()

    abstract fun isConnected(): Boolean

    suspend fun sendQuery(query: ByteArray): ByteArray = suspendCoroutine { continuation ->
        val (queryId, deferred) = queryLock.withLock {
            val queryId = nextQueryId()
            val deferred = CompletableDeferred<AdnlMessageAnswer>()
            queryMap[queryId] = deferred
            queryId to deferred
        }
        val adnlMessageQuery = AdnlMessageQuery(queryId.toByteArray(), query)
        val packet = AdnlMessageQuery.encodeBoxed(adnlMessageQuery)
        CoroutineScope(continuation.context).launch {
            var retry = 1
            while (true) {
                try {
                    outputFlow.emit(packet)
                    val answer = withTimeoutOrNull(3000) {
                        deferred.await().answer
                    }
                    if (answer != null) {
                        continuation.resume(answer)
                        break
                    }
                } catch (e: Exception) {
                    if (!isConnected()) {
                        connect()
                    }
                    logger.warn { "Retry... $retry" }
                    if (retry++ == 3) {
                        continuation.resumeWithException(e)
                        break
                    }
                }
            }
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

    protected fun launchIoJob() {
        supervisorJob = SupervisorJob()
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
                        val queryId = BitString(adnlPacket.query_id)
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
        sentPacketCount++
    }

    protected suspend fun receiveRaw(): ByteArray {
        val length = input.readIntLittleEndian()

        check(length >= 64) { "Too small packet: $length" }

        val nonce = ByteArray(32).also {
            input.readFully(it)
        }
        val payload = ByteArray(length - 64).also {
            input.readFully(it)
        }
        val hash = ByteArray(32).also {
            input.readFully(it)
        }

        val actualHash = sha256(nonce, payload)

        logger.debug { "RECEIVE: hash:${hash.encodeHex()} length:$length nonce:${hex(nonce)} payload:${hex(payload)}" }

        check(hash.contentEquals(actualHash)) {
            "[rcv=$receivedPacketCount snt=$sentPacketCount] Invalid hash! expected: ${hex(hash)} actual: ${
                hex(
                    actualHash
                )
            } length:$length nonce:${hex(nonce)} payload: ${
                hex(payload)
            }"
        }
        receivedPacketCount++
        return payload
    }

    private fun nextQueryId(): BitString {
        var nextId: BitString
        while (true) {
            nextId = BitString(SecureRandom.nextBytes(256 / Byte.SIZE_BITS))
            if (!queryMap.containsKey(nextId)) break
        }
        return nextId
    }

    private object AdnlTlCombinator : TlCombinator<Any>(
        AdnlMessage + AdnlPing + AdnlPong
    )
}
