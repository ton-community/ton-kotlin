@file:Suppress("OPT_IN_USAGE")

package org.ton.api.id.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.api.pk.PrivateKey

@Serializable
@SerialName("id.config.local")
@JsonClassDiscriminator("@type")
data class IdConfigLocal(
    val id: PrivateKey
)
