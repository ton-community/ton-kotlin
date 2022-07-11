package org.ton.adnl.node

import io.ktor.util.collections.*
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
    scope: CoroutineContext,
    val logger: Logger = PrintLnLogger("ADNL")
) : CoroutineScope {
    private val publicKey: PublicKey by lazy { privateKey.publicKey() }
    protected val supervisorJob = SupervisorJob()
    override val coroutineContext: CoroutineContext = scope + supervisorJob

    private val peers = ConcurrentMap<PublicKey, AdnlNode>()

    fun addPeer(peer: AdnlPeer): List<AdnlAddress>? {
        val adnlNode = peer.node
        if (adnlNode.id == privateKey.publicKey()) return null
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
