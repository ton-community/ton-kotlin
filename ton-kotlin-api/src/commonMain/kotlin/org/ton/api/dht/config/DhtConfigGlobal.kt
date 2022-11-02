package org.ton.api.dht.config

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.dht.DhtNode
import org.ton.api.dht.DhtNodes
import org.ton.tl.TlConstructor
import org.ton.tl.TlObject
import org.ton.tl.constructors.readIntTl
import org.ton.tl.constructors.writeIntTl
import org.ton.tl.readTl
import org.ton.tl.writeTl

@SerialName("dht.config.global")
@Serializable
data class DhtConfigGlobal(
    @SerialName("static_nodes")
    val staticNodes: DhtNodes,
    val k: Int,
    val a: Int
) : TlObject<DhtConfigGlobal> {
    constructor(
        staticNodes: List<DhtNode>,
        k: Int,
        a: Int
    ) : this(DhtNodes(staticNodes), k, a)

    override fun tlCodec() = Companion

    companion object : TlConstructor<DhtConfigGlobal>(
        type = DhtConfigGlobal::class,
        schema = "dht.config.global static_nodes:dht.nodes k:int a:int = dht.config.Global"
    ) {
        override fun encode(output: Output, value: DhtConfigGlobal) {
            output.writeTl(value.staticNodes)
            output.writeIntTl(value.k)
            output.writeIntTl(value.a)
        }

        override fun decode(input: Input): DhtConfigGlobal {
            val staticNodes = input.readTl(DhtNodes)
            val k = input.readIntTl()
            val a = input.readIntTl()
            return DhtConfigGlobal(staticNodes, k, a)
        }
    }
}
