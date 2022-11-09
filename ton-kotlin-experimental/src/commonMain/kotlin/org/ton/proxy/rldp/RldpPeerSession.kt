package org.ton.proxy.rldp

import io.ktor.util.collections.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.buffer
import kotlinx.datetime.Clock
import org.ton.api.adnl.message.AdnlMessageCustom
import org.ton.api.adnl.message.AdnlMessageQuery
import org.ton.api.pk.PrivateKeyEd25519
import org.ton.api.pub.PublicKeyEd25519
import org.ton.api.rldp.RldpAnswer
import org.ton.api.rldp.RldpMessageData
import org.ton.api.rldp.RldpMessagePart
import org.ton.api.rldp.RldpQuery
import org.ton.bitstring.BitString
import org.ton.logger.Logger
import org.ton.proxy.adnl.AbstractAdnlPeerSession
import org.ton.proxy.adnl.AdnlPeerSession
import kotlin.experimental.xor
import kotlin.random.Random
import kotlin.time.Duration

interface RldpPeerSession : AdnlPeerSession {
    val rldp: Rldp
}

abstract class AbstractRldpPeerSession(
    override val rldp: Rldp,
    localKey: PrivateKeyEd25519,
    remoteKey: PublicKeyEd25519
) : RldpPeerSession, AbstractAdnlPeerSession(rldp, localKey, remoteKey) {
    private val receivers = ConcurrentMap<BitString, RldpReceiver>()
    private val logger = Logger.println(toString())

    suspend fun transfer(
        data: ByteArray,
        id: BitString = BitString(Random.nextBytes(32))
    ) = transfer(data.size.toLong(), ByteReadPacket(data), id)

    suspend fun transfer(
        totalLength: Long,
        input: Input,
        id: BitString = BitString(Random.nextBytes(32)),
    ) {
        val transfer = RldpOutputTransfer.of(totalLength, input, id)
        receivers[id] = transfer
        try {
            transfer.transferPackets().buffer().collect { part ->
                super.message(part.toByteArray())
            }
        } finally {
            receivers.remove(id)
        }
    }

    suspend fun <T> inputTransfer(
        id: BitString,
        read: suspend (ByteReadChannel) -> T
    ) = coroutineScope {
        val transfer = RldpInputTransfer.of(id)
        receivers[id] = transfer
        try {
            val byteChannel = ByteChannel()
            transfer.transferPackets().buffer().collect { part ->
                super.message(part.toByteArray())
            }
            val readJob = async {
                val result = read(byteChannel)
                byteChannel.close()
                result
            }
            transfer.byteChannel.copyAndClose(byteChannel)
            readJob.await()
        } finally {
            receivers.remove(id)
        }
    }

    override suspend fun message(payload: ByteArray) {
        val rldpMessage = RldpMessageData(
            id = BitString(Random.nextBytes(32)),
            data = payload
        )
        transfer(rldpMessage.toByteArray())
    }

    override suspend fun query(payload: ByteArray, timeout: Duration, maxAnswerSize: Long): ByteArray = coroutineScope {
        val outputTransferId = Random.nextBytes(32)
        val inputTransferId = outputTransferId.map { it xor 0xFF.toByte() }.toByteArray()
        val rldpQuery =
            RldpQuery(BitString(Random.nextBytes(32)), maxAnswerSize, Clock.System.now() + timeout, payload)
        val rldpAnswer = async {
            inputTransfer(BitString(inputTransferId)) {
                RldpAnswer.decodeBoxed(it)
            }
        }
        transfer(rldpQuery.toByteArray(), BitString(outputTransferId))
        rldpAnswer.await().data
    }

    override fun receiveAdnlCustom(message: AdnlMessageCustom) {
        val part = RldpMessagePart.decodeBoxed(message.data)
        val receiver = receivers[part.transfer_id]
        receiver?.receiveRldpMessagePart(part)
    }

    override fun receiveAdnlQuery(message: AdnlMessageQuery) {
        throw IllegalStateException("RLDP does not support ADNL queries")
    }

    override fun toString(): String = "RLDP[${localKey}<->${remoteKey.toAdnlIdShort()}]@${hashCode()}"
}
