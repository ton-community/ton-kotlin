package org.ton.adnl.node

import org.ton.api.adnl.AdnlProxy as AdnlProxyType

class AdnlProxy(
    val type: AdnlProxyType
) {
    init {
        TODO()
    }

    val id get() = type.id

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AdnlProxy) return false
        if (type != other.type) return false
        return true
    }
}
