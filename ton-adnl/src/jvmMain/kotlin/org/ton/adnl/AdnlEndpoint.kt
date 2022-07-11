package org.ton.adnl

import kotlinx.atomicfu.AtomicInt
import kotlinx.atomicfu.AtomicLong
import kotlinx.coroutines.CoroutineScope
import org.ton.api.adnl.AdnlAddress
import org.ton.api.adnl.AdnlAddressList
import org.ton.api.adnl.AdnlPacketContents
import org.ton.api.adnl.message.AdnlMessage
import org.ton.api.pk.PrivateKey
import org.ton.api.pub.PublicKey
import org.ton.crypto.SecureRandom
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random

class AdnlEndpoint(
    override val coroutineContext: CoroutineContext
) : CoroutineScope {
    fun sendPacket(
        peer: Peer,
        addressList: AdnlAddressList,
        source: PrivateKey,
        priority: Boolean,
        channel: AdnlChannel? = null,
        vararg messages: AdnlMessage
    ) {
        @Suppress("NAME_SHADOWING")
        var priority = priority
        val repeat = if (priority) {
            if (channel == null) {
                // No need if no channel
                priority = false
            } else {
                if (peer.receiveState.seqno(priority = true) == 0L) {
                    if (peer.sendState.seqno(priority = true) > MAX_PRIORITY_ATTEMPTS) {
                        priority = false
                    }
                }
            }
            if (priority && (peer.receiveState.seqno(priority = true) == 0L)) {
                MessageRepeat.REQUIRED
            } else {
                MessageRepeat.NOT_NEEDED
            }
        } else {
            MessageRepeat.UNAPPLICABLE
        }
        val sourcePublicKey = source.publicKey()
        var packet = AdnlPacketContents(
            rand1 = generateNonce(),
            from = if (channel != null) null else sourcePublicKey,
            from_short = if (channel != null) null else sourcePublicKey.toAdnlIdShort(),
            message = if (messages.size == 1) messages[0] else null,
            messages = if (messages.size > 1) messages.toList() else null,
            address = addressList,
            priority_address = null,
            seqno = peer.sendState.nextSeqno(priority),
            confirm_seqno = peer.receiveState.seqno(priority),
            recv_addr_list_version = null,
            recv_priority_addr_list_version = null,
            reinit_date = if (channel != null) null else peer.receiveState.reinitDate.value,
            dst_reinit_date = if (channel != null) null else peer.sendState.reinitDate.value,
            signature = null,
            rand2 = generateNonce()
        )
        if (channel == null) {
            val signature = source.sign(packet.toByteArray())
            packet = packet.copy(signature = signature)
        }
    }

    fun generateNonce(random: Random = SecureRandom) = random.nextBytes(16)

    companion object {
        const val MAX_PRIORITY_ATTEMPTS = 10
    }
}

enum class MessageRepeat {
    NOT_NEEDED,
    REQUIRED,
    UNAPPLICABLE
}

data class Peer(
    val address: AdnlAddress,
    val receiveState: PeerState,
    val sendState: PeerState
)

data class AdnlChannel(
    val localKey: PrivateKey,
    val otherKey: PublicKey
)

data class PeerHistory(
    val seqno: AtomicLong
)

data class PeerState(
    val ordinaryHistory: PeerHistory,
    val priorityHistory: PeerHistory,
    val reinitDate: AtomicInt
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