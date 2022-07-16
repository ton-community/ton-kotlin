@file:Suppress("OPT_IN_USAGE")

package org.ton.api.dht.config

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@SerialName("dht.config.random.local")
@Polymorphic
@Serializable
@JsonClassDiscriminator("@type")
class DhtConfigRandomLocal(
    val cnt: Int
) : DhtConfigLocal