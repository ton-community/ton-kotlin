@file:Suppress("OPT_IN_USAGE")

package org.ton.api.dht.config

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.api.adnl.AdnlIdShort

@SerialName("dht.config.local")
@Polymorphic
@Serializable
@JsonClassDiscriminator("@type")
data class DhtConfigIdLocal(
    val id: AdnlIdShort
) : DhtConfigLocal