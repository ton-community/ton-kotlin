package org.ton.adnl.node

import org.ton.api.adnl.AdnlIdShort
import org.ton.api.adnl.AdnlNode

public interface Adnl {
    public fun addPeer(localId: AdnlIdShort, node: AdnlNode)
}
