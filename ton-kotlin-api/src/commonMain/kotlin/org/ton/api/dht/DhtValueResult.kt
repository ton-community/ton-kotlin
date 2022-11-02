@file:Suppress("OPT_IN_USAGE")

package org.ton.api.dht

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tl.*

@Serializable
@JsonClassDiscriminator("@type")
sealed interface DhtValueResult : TlObject<DhtValueResult> {
    fun value(): DhtValue?

    override fun tlCodec(): TlCodec<out DhtValueResult> = Companion

    companion object : TlCombinator<DhtValueResult>(
        DhtValueNotFound,
        DhtValueFound
    )
}

@Serializable
@SerialName("dht.valueNotFound")
data class DhtValueNotFound(
    val nodes: DhtNodes
) : DhtValueResult {
    override fun value(): DhtValue? = null


    companion object : TlConstructor<DhtValueNotFound>(
        type = DhtValueNotFound::class,
        schema = "dht.valueNotFound nodes:dht.nodes = dht.ValueResult"
    ) {
        override fun encode(output: Output, value: DhtValueNotFound) {
            output.writeTl(DhtNodes, value.nodes)
        }

        override fun decode(input: Input): DhtValueNotFound {
            val nodes = input.readTl(DhtNodes)
            return DhtValueNotFound(nodes)
        }
    }
}

@Serializable
@SerialName("dht.valueFound")
data class DhtValueFound(
    val value: DhtValue
) : DhtValueResult {
    override fun value(): DhtValue = value

    companion object : TlConstructor<DhtValueFound>(
        type = DhtValueFound::class,
        schema = "dht.valueFound value:dht.Value = dht.ValueResult"
    ) {
        override fun encode(output: Output, value: DhtValueFound) {
            DhtValue.encodeBoxed(value.value)
            output.writeTl(DhtValue, value.value)
        }

        override fun decode(input: Input): DhtValueFound {
            val value = DhtValue.decodeBoxed(input)
            return DhtValueFound(value)
        }
    }
}
