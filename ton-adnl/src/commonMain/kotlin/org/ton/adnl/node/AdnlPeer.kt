package org.ton.adnl.node

import kotlinx.atomicfu.AtomicInt
import kotlinx.atomicfu.AtomicLong
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CoroutineScope
import kotlinx.datetime.Clock
import org.ton.api.adnl.AdnlAddress
import org.ton.api.adnl.AdnlNode
import org.ton.api.adnl.AdnlPacketContents
import org.ton.api.adnl.message.AdnlMessage
import org.ton.api.pk.PrivateKey
import org.ton.crypto.SecureRandom
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random

class AdnlPeer(
    val engine: AdnlNodeEngine,
    val address: AdnlAddress,
    val node: AdnlNode,
    override val coroutineContext: CoroutineContext
) : CoroutineScope {
    private val receiveState = PeerState()
    private val sendState = PeerState()

    suspend fun sendMessage(
        source: PrivateKey,
        priority: Boolean,
        channel: AdnlChannel? = null,
        vararg messages: AdnlMessage
    ): MessageRepeat {
        @Suppress("NAME_SHADOWING")
        var priority = priority
        val repeat = if (priority) {
            if (channel == null) {
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
            rand1 = generateRandom(),
            from = if (channel != null) null else sourcePublicKey,
            from_short = if (channel != null) null else sourcePublicKey.toAdnlIdShort(),
            message = if (messages.size == 1) messages[0] else null,
            messages = if (messages.size > 1) messages.toList() else null,
            address = node.addrList,
            priority_address = null,
            seqno = sendState.nextSeqno(priority),
            confirm_seqno = receiveState.seqno(priority),
            recv_addr_list_version = null,
            recv_priority_addr_list_version = null,
            reinit_date = if (channel != null) null else receiveState.reinitDate.value,
            dst_reinit_date = if (channel != null) null else sendState.reinitDate.value,
            signature = null,
            rand2 = generateRandom()
        )
        if (channel == null) {
            val signature = source.sign(packet.toByteArray())
            packet = packet.copy(signature = signature)
            engine.sendPacket(this, packet, null)
        } else {
            val subChannelSide = if (priority) {
                channel.send.priority
            } else {
                channel.send.ordinary
            }
            engine.sendPacket(this, packet, subChannelSide)
        }
        return repeat
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