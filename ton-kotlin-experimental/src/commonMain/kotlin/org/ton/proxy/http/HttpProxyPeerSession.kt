package org.ton.proxy.http

import org.ton.api.pk.PrivateKeyEd25519
import org.ton.api.pub.PublicKeyEd25519
import org.ton.proxy.rldp.AbstractRldpPeerSession
import org.ton.proxy.rldp.RldpPeerSession

interface HttpProxyPeerSession : RldpPeerSession {
    val httpProxy: HttpProxy
}

abstract class AbstractHttpProxyPeerSession(
    override val httpProxy: HttpProxy,
    localKey: PrivateKeyEd25519,
    remoteKey: PublicKeyEd25519
) : AbstractRldpPeerSession(httpProxy, localKey, remoteKey), HttpProxyPeerSession {

}
