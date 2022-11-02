package org.ton.proxy.dht

import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import org.ton.api.adnl.AdnlAddressList
import org.ton.api.adnl.AdnlIdShort
import org.ton.api.dht.DhtKey
import org.ton.api.dht.DhtValue
import org.ton.api.dht.DhtValueFound
import org.ton.proxy.adnl.AdnlAddressResolver
import org.ton.proxy.dht.state.DhtState
import org.ton.proxy.dht.storage.DhtStorage

class Dht(
    val state: DhtState,
    val config: DhtConfig = DhtConfig()
) : AdnlAddressResolver {
    suspend fun findValue(key: DhtKey): DhtValue? = findValue(key.key())

    suspend fun findValue(key: ByteArray): DhtValue? {
       return DhtValuesFlow(key, state.knownPeers).map { (peer,result) ->
            val peerId = state.addPeer(peer)
            state.updatePeerStatus(peerId, result != null)
            result
        }.filterIsInstance<DhtValueFound>().firstOrNull()?.value
    }

    override suspend fun resolve(address: AdnlIdShort): AdnlAddressList? {
        val value = findValue(DhtKey.address(address)) ?: return null
        return AdnlAddressList.decodeBoxed(value.value)
    }

    companion object {
        @JvmStatic
        fun full(
            localId: AdnlIdShort,
            knownPeers: Collection<DhtPeer>,
            storage: DhtStorage
        ) = Dht(DhtState.full(localId, knownPeers, storage))

        @JvmStatic
        fun lite(
            knownPeers: Collection<DhtPeer>
        ) = Dht(DhtState.lite(knownPeers))
    }
}
