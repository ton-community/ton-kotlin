package org.ton.api.dht.db

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.api.dht.DhtNodes
import org.ton.tl.TlConstructor

@Serializable
data class DhtDbBucket(
        val nodes: DhtNodes
) {
    companion object : TlConstructor<DhtDbBucket>(
            type = DhtDbBucket::class,
            schema = "dht.db.bucket nodes:dht.nodes = dht.db.Bucket"
    ) {
        override fun encode(output: Output, message: DhtDbBucket) {
            output.writeTl(message.nodes, DhtNodes)
        }

        override fun decode(input: Input): DhtDbBucket {
            val nodes = input.readTl(DhtNodes)
            return DhtDbBucket(nodes)
        }
    }
}