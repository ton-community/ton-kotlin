package org.ton.adnl

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import org.ton.api.adnl.AdnlAddress
import org.ton.api.adnl.AdnlAddressUdp
import org.ton.api.adnl.AdnlNode
import org.ton.api.pub.PublicKey
import org.ton.logger.Logger
import org.ton.logger.PrintLnLogger
import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.CoroutineContext

class AdnlNodeImpl(
    val config: AdnlNode,
    val scope: CoroutineContext,
    val logger: Logger = PrintLnLogger("ADNL")
) : CoroutineScope {
    protected val supervisorJob = SupervisorJob()
    override val coroutineContext: CoroutineContext = scope + supervisorJob

    private val peers = ConcurrentHashMap<PublicKey, AdnlNode>()

    private lateinit var socket: BoundDatagramSocket
    val outgoing get() = socket.outgoing
    val incoming get() = socket.incoming

    val outgoingFlow = MutableSharedFlow<ByteArray>()

    fun start() {
        val bindAddress = config.addrList.filterIsInstance<AdnlAddressUdp>().first()
        socket = aSocket(SelectorManager()).udp().bind(
            localAddress = InetSocketAddress(
                ipv4(bindAddress.ip),
                bindAddress.port
            )
        )
    }

    fun addPeer(adnlNode: AdnlNode): List<AdnlAddress>? {
        if (adnlNode.id == config.id) return null
        val currentIdNode = peers[adnlNode.id]
        val newAddresses = if (currentIdNode != null) {
            val newAddressList = (currentIdNode.addrList.addrs + adnlNode.addrList.addrs).distinct()
            peers[adnlNode.id] = adnlNode.copy(
                addrList = adnlNode.addrList.copy(
                    addrs = newAddressList.distinct()
                )
            )
            adnlNode.addrList.asSequence().filter { address ->
                address !in currentIdNode.addrList
            }
        } else {
            peers[adnlNode.id] = adnlNode
            adnlNode.addrList.asSequence()
        }.toList()
        logger.debug { "Added ADNL peer with addresses: $newAddresses, id: ${adnlNode.id} to: ${config.id}" }
        return newAddresses
    }

    fun removePeer(adnlNode: AdnlNode) = removePeer(adnlNode.id)
    fun removePeer(publicKey: PublicKey) {
        if (peers.remove(publicKey) == null) {
            logger.warn { "Try to remove peer: $publicKey from unknown node: ${config.id}" }
        }
    }
}
