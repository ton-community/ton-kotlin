@file:Suppress("OPT_IN_USAGE")

package org.ton.api.dht.config

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@Polymorphic
@Serializable
@JsonClassDiscriminator("@type")
sealed interface DhtConfigLocal