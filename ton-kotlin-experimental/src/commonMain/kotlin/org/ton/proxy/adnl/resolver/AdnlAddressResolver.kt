package org.ton.proxy.adnl.resolver

import org.ton.api.adnl.AdnlAddress
import org.ton.api.adnl.AdnlAddressList
import org.ton.api.adnl.AdnlIdShort
import org.ton.api.pub.PublicKey

fun interface AdnlAddressResolver {
    suspend fun resolve(publicKey: PublicKey): List<AdnlAddress>? = resolve(publicKey.toAdnlIdShort())?.second
    suspend fun resolve(address: AdnlIdShort): Pair<PublicKey, List<AdnlAddress>>?
}
