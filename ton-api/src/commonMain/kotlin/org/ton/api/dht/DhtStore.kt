package org.ton.api.dht

import io.ktor.utils.io.core.*
import org.ton.tl.*

data class DhtStore(
    val value: DhtValue
) : TlObject<DhtStore> {
    override fun tlCodec(): TlCodec<DhtStore> = DhtStoreTlConstructor

    companion object : TlCodec<DhtStore> by DhtStoreTlConstructor
}

private object DhtStoreTlConstructor : TlConstructor<DhtStore>(
    type = DhtStore::class,
    schema = "dht.store value:dht.value = dht.Stored"
) {
    override fun decode(input: Input): DhtStore {
        val value = input.readTl(DhtValue)
        return DhtStore(value)
    }

    override fun encode(output: Output, value: DhtStore) {
        output.writeTl(DhtValue, value.value)
    }
}
