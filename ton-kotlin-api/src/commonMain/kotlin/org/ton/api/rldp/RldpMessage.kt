@file:Suppress("OPT_IN_USAGE")

package org.ton.api.rldp

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tl.TlCombinator
import org.ton.tl.TlObject

@Polymorphic
@Serializable
@JsonClassDiscriminator("@type")
sealed interface RldpMessage : TlObject<RldpMessage> {
    val id: ByteArray
    val data: ByteArray

    companion object : TlCombinator<RldpMessage>(
        RldpMessageData,
        RldpQuery,
        RldpAnswer,
    )
}
