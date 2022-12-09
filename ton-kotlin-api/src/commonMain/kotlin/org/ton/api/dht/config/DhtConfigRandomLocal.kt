@file:Suppress("OPT_IN_USAGE")

package org.ton.api.dht.config

import io.ktor.utils.io.core.*
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readIntTl
import org.ton.tl.constructors.writeIntTl

@SerialName("dht.config.random.local")
@Polymorphic
@Serializable
@JsonClassDiscriminator("@type")
class DhtConfigRandomLocal(
    val cnt: Int
) : DhtConfigLocal {
    companion object : TlConstructor<DhtConfigRandomLocal>(
        schema = "dht.config.random.local cnt:int = dht.config.Local",
    ) {
        override fun encode(output: Output, value: DhtConfigRandomLocal) {
            output.writeIntTl(value.cnt)
        }

        override fun decode(input: Input): DhtConfigRandomLocal {
            val cnt = input.readIntTl()
            return DhtConfigRandomLocal(cnt)
        }
    }
}
