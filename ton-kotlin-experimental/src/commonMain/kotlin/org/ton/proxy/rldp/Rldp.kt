package org.ton.proxy.rldp

import kotlinx.coroutines.CoroutineScope
import org.ton.api.adnl.AdnlIdShort
import org.ton.api.pk.PrivateKey
import org.ton.api.pk.PrivateKeyEd25519
import org.ton.api.pub.PublicKey
import org.ton.api.pub.PublicKeyEd25519
import org.ton.proxy.adnl.Adnl
import org.ton.proxy.adnl.engine.AdnlNetworkEngine
import org.ton.proxy.adnl.resolver.AdnlAddressResolver
import kotlin.time.Duration

class Rldp(
    networkEngine: AdnlNetworkEngine,
    addressResolver: AdnlAddressResolver
) : CoroutineScope, Adnl(networkEngine, addressResolver) {
    override fun createPeer(remoteKey: PublicKey, localKey: PrivateKey): RldpPeerSession {
        require(remoteKey is PublicKeyEd25519)
        require(localKey is PrivateKeyEd25519)
        return object : AbstractRldpPeerSession(this@Rldp, localKey, remoteKey) {}
    }

    override suspend fun message(destination: AdnlIdShort, payload: ByteArray) =
        getPeer(destination).message(payload)

    override suspend fun query(
        destination: AdnlIdShort,
        payload: ByteArray,
        timeout: Duration,
        maxAnswerSize: Long
    ): ByteArray = getPeer(destination).query(payload, timeout, maxAnswerSize)
}
