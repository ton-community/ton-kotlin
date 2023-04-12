package org.ton.adnl.client

import org.ton.adnl.ipv4
import org.ton.adnl.resolver.MapAdnlAddressResolver
import org.ton.api.adnl.AdnlAddressUdp
import org.ton.api.pk.PrivateKeyEd25519
import kotlin.test.Test

class AdnlTest {
    @Test
    fun a() {
        val key1 = PrivateKeyEd25519()
        val key2 = PrivateKeyEd25519()

        val resolver = MapAdnlAddressResolver(
            map = mapOf(
                key1.toAdnlIdShort() to (key1.publicKey() to listOf(
                    AdnlAddressUdp(ipv4("0.0.0.0"), 1000)
                )),
                key2.toAdnlIdShort() to (key2.publicKey() to listOf(
                    AdnlAddressUdp(ipv4("0.0.0.0"), 2000)
                ))
            )
        )

    }
}
