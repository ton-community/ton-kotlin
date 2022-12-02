package org.ton.api.control

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.api.pk.PrivateKey
import org.ton.crypto.base64.Base64ByteArraySerializer

@Serializable
@Polymorphic
@SerialName("control.config.local")
@JsonClassDiscriminator("@type")
data class ControlConfigLocal(
    val priv: PrivateKey,
    @Serializable(Base64ByteArraySerializer::class)
    val pub: ByteArray,
    val port: Int
)
