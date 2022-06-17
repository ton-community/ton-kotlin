package org.ton.api.dht

interface DhtApi {
    suspend fun ping(randomId: Long): DhtPong

    suspend fun store(value: DhtValue): DhtStored

    suspend fun findNode(key: DhtKey, k: Int): DhtNodes

    suspend fun findValue(key: DhtKey, k: Int): DhtValueResult

    suspend fun getSignedAddressList(): DhtNode

    suspend fun query(node: DhtNode): Boolean
}