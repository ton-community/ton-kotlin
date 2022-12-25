package org.ton.proxy.adnl

import kotlinx.datetime.Instant
import org.ton.api.pub.PublicKey

class Channel(
    val publicKey: PublicKey,
    val date: Instant
) {

}
