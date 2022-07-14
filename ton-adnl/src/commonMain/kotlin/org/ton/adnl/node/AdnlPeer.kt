package org.ton.adnl.node

import io.ktor.util.collections.*
import kotlinx.atomicfu.AtomicInt
import kotlinx.atomicfu.AtomicLong
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import org.ton.api.adnl.AdnlNode
import org.ton.api.adnl.AdnlPacketContents
import org.ton.api.adnl.message.AdnlMessage
import org.ton.api.adnl.message.AdnlMessageAnswer
import org.ton.api.pk.PrivateKey
import org.ton.crypto.SecureRandom
import org.ton.tl.TlCodec
import org.ton.tl.TlObject
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random

class AdnlPeer(
    val engine: AdnlNodeEngine,
    val node: AdnlNode,
    override val coroutineContext: CoroutineContext
) : CoroutineScope {
    private val receiveState = PeerState()
    private val sendState = PeerState()
    private val queries = ConcurrentMap<QueryId, Query<*, *>>()
    private val queryMutex = Mutex()

    // TODO: Rewrite for concurrency
    suspend fun <Q : TlObject<Q>, A : TlObject<A>> query(
        source: PrivateKey,
        queryBody: Q,
        answerCodec: TlCodec<A>
    ): A {
        val query = Query(queryBody, answerCodec)
        queries[query.id] = query
        val queryMsg = query.createMessageQuery()

        queryMutex.withLock {
            sendMessage(source, true, AdnlChannel.zero(), queryMsg)
            val answerMsg = receiveMessages()
                .filterIsInstance<AdnlMessageAnswer>()
                .first { it.query_id.contentEquals(query.id.value) }
            return answerCodec.decodeBoxed(answerMsg.answer)
        }
    }

    suspend fun sendMessage(
        source: PrivateKey,
        priority: Boolean,
        channel: AdnlChannel,
        vararg messages: AdnlMessage
    ): MessageRepeat {
        @Suppress("NAME_SHADOWING")
        var priority = priority
        val repeat = if (priority) {
            if (channel == AdnlChannel.zero()) {
                // No need if no channel
                priority = false
            } else {
                if (receiveState.seqno(priority = true) == 0L) {
                    if (sendState.seqno(priority = true) > MAX_PRIORITY_ATTEMPTS) {
                        priority = false
                    }
                }
            }
            if (priority && (receiveState.seqno(priority = true) == 0L)) {
                MessageRepeat.REQUIRED
            } else {
                MessageRepeat.NOT_NEEDED
            }
        } else {
            MessageRepeat.UNAPPLICABLE
        }
        val sourcePublicKey = source.publicKey()
        var packet = AdnlPacketContents(
            from = if (channel != AdnlChannel.zero()) null else sourcePublicKey,
            from_short = if (channel != AdnlChannel.zero()) null else sourcePublicKey.toAdnlIdShort(),
            message = if (messages.size == 1) messages[0] else null,
            messages = if (messages.size > 1) messages.toList() else null,
            address = if (!priority) node.addr_list else null,
            priority_address = if (priority) node.addr_list else null,
            seqno = sendState.nextSeqno(priority),
            confirm_seqno = receiveState.seqno(priority),
            recv_addr_list_version = null,
            recv_priority_addr_list_version = null,
            reinit_date = if (channel != AdnlChannel()) null else receiveState.reinitDate.value,
            dst_reinit_date = if (channel != AdnlChannel()) null else sendState.reinitDate.value,
        )
        val subChannelSide = if (priority) {
            channel.send.priority
        } else {
            channel.send.ordinary
        }
        if (channel == AdnlChannel.zero()) {
            val signature = source.sign(packet.toByteArray())
            packet = packet.copy(signature = signature)
            engine.sendPacket(packet, sourcePublicKey)
        } else {
            engine.sendPacket(packet, subChannelSide)
        }
        return repeat
    }

    suspend fun receiveMessages(): List<AdnlMessage> {
        val packet = engine.receivePacket()
        return packet.messages()
    }

    private fun generateRandom(random: Random = SecureRandom) = random.nextBytes(16)

    companion object {
        const val MAX_PRIORITY_ATTEMPTS = 10
    }
}

enum class MessageRepeat {
    NOT_NEEDED,
    REQUIRED,
    UNAPPLICABLE
}

data class PeerHistory(
    val seqno: AtomicLong = atomic(0L)
)

data class PeerState(
    val ordinaryHistory: PeerHistory = PeerHistory(),
    val priorityHistory: PeerHistory = PeerHistory(),
    val reinitDate: AtomicInt = atomic(Clock.System.now().epochSeconds.toInt())
) {
    fun nextSeqno(priority: Boolean): Long = if (priority) {
        priorityHistory.seqno.incrementAndGet()
    } else {
        ordinaryHistory.seqno.incrementAndGet()
    }

    fun seqno(priority: Boolean): Long = if (priority) {
        priorityHistory.seqno.value
    } else {
        ordinaryHistory.seqno.value
    }
}