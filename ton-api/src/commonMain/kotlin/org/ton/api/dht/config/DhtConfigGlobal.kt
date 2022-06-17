package org.ton.api.dht.config

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.dht.DhtNodes
import org.ton.tl.TlConstructor
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
) {
    companion object : TlConstructor<DhtConfigGlobal>(
        type = DhtConfigGlobal::class,
        schema = "dht.config.global static_nodes:dht.nodes k:int a:int = dht.config.Global"
    ) {
        override fun encode(output: Output, value: DhtConfigGlobal) {
            output.writeTl(DhtNodes, value.staticNodes)
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
