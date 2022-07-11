package org.ton.dht

import kotlinx.coroutines.coroutineScope
import org.ton.adnl.node.AdnlNodeEngineCIO
import org.ton.adnl.node.AdnlNodeImpl
import org.ton.api.pk.PrivateKeyEd25519

suspend fun main() = coroutineScope {
    val engine = AdnlNodeEngineCIO()
    val adnl = AdnlNodeImpl(PrivateKeyEd25519.random(), coroutineContext)
}