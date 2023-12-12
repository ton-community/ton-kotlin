package org.ton.adnl.resolver

import org.ton.api.adnl.AdnlAddress
import org.ton.api.adnl.AdnlIdShort
import org.ton.api.pub.PublicKey

public interface AdnlAddressResolver {
    public suspend fun resolve(publicKey: PublicKey): List<AdnlAddress>? = resolve(publicKey.toAdnlIdShort())?.second
    public suspend fun resolve(address: AdnlIdShort): Pair<PublicKey, List<AdnlAddress>>?
}
