package org.ton.api.dht.db

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.api.dht.DhtNodes
import org.ton.tl.TlConstructor
import org.ton.tl.readTl
import org.ton.tl.writeTl

@Serializable
data class DhtDbBucket(
        val nodes: DhtNodes
) {
    companion object : TlConstructor<DhtDbBucket>(
            type = DhtDbBucket::class,
            schema = "dht.db.bucket nodes:dht.nodes = dht.db.Bucket"
    ) {
        override fun encode(output: Output, value: DhtDbBucket) {
            output.writeTl(DhtNodes, value.nodes)
        }

        override fun decode(input: Input): DhtDbBucket {
            val nodes = input.readTl(DhtNodes)
            return DhtDbBucket(nodes)
        }
    }
}
