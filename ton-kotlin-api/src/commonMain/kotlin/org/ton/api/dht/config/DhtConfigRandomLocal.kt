@file:Suppress("OPT_IN_USAGE")

package org.ton.api.dht.config

import io.ktor.utils.io.core.*
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tl.*

@SerialName("dht.config.random.local")
@Polymorphic
@Serializable
@JsonClassDiscriminator("@type")
public class DhtConfigRandomLocal(
    public val cnt: Int
) : DhtConfigLocal {
    public companion object : TlConstructor<DhtConfigRandomLocal>(
        schema = "dht.config.random.local cnt:int = dht.config.Local",
    ) {
        override fun encode(output: TlWriter, value: DhtConfigRandomLocal) {
            output.writeInt(value.cnt)
        }

        override fun decode(input: TlReader): DhtConfigRandomLocal {
            val cnt = input.readInt()
            return DhtConfigRandomLocal(cnt)
        }
    }
}
