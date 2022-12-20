package org.ton.adnl.node

import org.ton.api.adnl.AdnlIdShort
import org.ton.api.adnl.AdnlNode
import org.ton.logger.Logger

public class AdnlPeerTable : Adnl {

    private val log = Logger.println(toString())

    override fun addPeer(localId: AdnlIdShort, node: AdnlNode) {
        val idShort = node.id.toAdnlIdShort()
        log.debug { "adding peer $idShort for local id $localId" }


    }

}
