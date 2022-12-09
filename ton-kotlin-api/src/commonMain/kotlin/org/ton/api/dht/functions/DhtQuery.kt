package org.ton.api.dht.functions

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.dht.DhtNode
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.readTl
import org.ton.tl.writeTl

interface DhtQueryFunction {
    fun query(node: DhtNode)
}

@SerialName("dht.query")
@Serializable
data class DhtQuery(
    val node: DhtNode
) {
    companion object : TlCodec<DhtQuery> by DhtQueryTlConstructor
}

private object DhtQueryTlConstructor : TlConstructor<DhtQuery>(
    schema = "dht.query node:dht.node = True"
) {
    override fun decode(input: Input): DhtQuery {
        val node = input.readTl(DhtNode)
        return DhtQuery(node)
    }

    override fun encode(output: Output, value: DhtQuery) {
        output.writeTl(DhtNode, value.node)
    }
}
