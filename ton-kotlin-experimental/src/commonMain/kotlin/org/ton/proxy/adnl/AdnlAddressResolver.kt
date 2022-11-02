package org.ton.proxy.adnl

import org.ton.api.adnl.AdnlAddressList
import org.ton.api.adnl.AdnlIdShort

fun interface AdnlAddressResolver {
    suspend fun resolve(address: AdnlIdShort): AdnlAddressList?
}
