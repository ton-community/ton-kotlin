package org.ton.adnl.node

import org.ton.api.adnl.AdnlIdShort
import org.ton.api.adnl.AdnlNode

interface Adnl {
    fun addPeer(localId: AdnlIdShort, node: AdnlNode)
}
