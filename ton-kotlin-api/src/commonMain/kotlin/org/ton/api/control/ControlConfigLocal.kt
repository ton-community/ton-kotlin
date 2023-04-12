package org.ton.api.control

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.api.pk.PrivateKey
import org.ton.tl.ByteString

@Serializable
@Polymorphic
@SerialName("control.config.local")
@JsonClassDiscriminator("@type")
public data class ControlConfigLocal(
    val priv: PrivateKey,
    val pub: ByteString,
    val port: Int
)
