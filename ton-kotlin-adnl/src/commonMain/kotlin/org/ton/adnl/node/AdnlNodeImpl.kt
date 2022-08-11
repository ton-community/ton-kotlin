package org.ton.adnl.node

import io.ktor.util.collections.*
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.ton.api.adnl.AdnlAddress
import org.ton.api.adnl.AdnlNode
import org.ton.api.pk.PrivateKey
import org.ton.api.pub.PublicKey
import org.ton.logger.Logger
import org.ton.logger.PrintLnLogger
import kotlin.coroutines.CoroutineContext

class AdnlNodeImpl(
    val privateKey: PrivateKey,
    scope: CoroutineContext = CoroutineName("ADNL Node"),
    val logger: Logger = PrintLnLogger("ADNL")
) : CoroutineScope {
    private val supervisorJob = SupervisorJob()
    private val peers = ConcurrentMap<PublicKey, AdnlNode>()

    val publicKey: PublicKey by lazy { privateKey.publicKey() }
    override val coroutineContext: CoroutineContext = scope + supervisorJob

    fun addPeer(peer: AdnlPeer): List<AdnlAddress>? {
        val adnlNode = peer.node
        if (adnlNode.id == privateKey.publicKey()) return null
        val currentIdNode = peers[adnlNode.id]
        val newAddresses = if (currentIdNode != null) {
            val newAddressList = (currentIdNode.addr_list.addrs + adnlNode.addr_list.addrs).distinct()
            peers[adnlNode.id] = adnlNode.copy(
                addr_list = adnlNode.addr_list.copy(
                    addrs = newAddressList.distinct()
                )
            )
            adnlNode.addr_list.asSequence().filter { address ->
                address !in currentIdNode.addr_list
            }
        } else {
            peers[adnlNode.id] = adnlNode
            adnlNode.addr_list.asSequence()
        }.toList()
        logger.debug { "Added ADNL peer with addresses: $newAddresses, id: ${adnlNode.id} to: $publicKey" }
        return newAddresses
    }

    fun removePeer(peer: AdnlPeer) = removePeer(peer.node)
    fun removePeer(adnlNode: AdnlNode) = removePeer(adnlNode.id)
    fun removePeer(publicKey: PublicKey) {
        if (peers.remove(publicKey) == null) {
            logger.warn { "Try to remove peer: $publicKey from unknown node: $publicKey" }
        }
    }
}
