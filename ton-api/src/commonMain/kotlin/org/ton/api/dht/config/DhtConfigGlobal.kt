package org.ton.api.dht.config

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.dht.DhtNodes
import org.ton.tl.TlConstructor

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
        override fun encode(output: Output, message: DhtConfigGlobal) {
            output.writeTl(message.staticNodes, DhtNodes)
            output.writeIntLittleEndian(message.k)
            output.writeIntLittleEndian(message.a)
        }

        override fun decode(input: Input): DhtConfigGlobal {
            val staticNodes = input.readTl(DhtNodes)
            val k = input.readIntLittleEndian()
            val a = input.readIntLittleEndian()
            return DhtConfigGlobal(staticNodes, k, a)
        }
    }
}