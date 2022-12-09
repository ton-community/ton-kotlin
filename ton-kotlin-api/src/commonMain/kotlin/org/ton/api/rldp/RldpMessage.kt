@file:Suppress("OPT_IN_USAGE")

package org.ton.api.rldp

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.bitstring.BitString
import org.ton.tl.TlCombinator
import org.ton.tl.TlObject

@Polymorphic
@Serializable
@JsonClassDiscriminator("@type")
sealed interface RldpMessage : TlObject<RldpMessage> {
    val id: BitString
    val data: ByteArray

    companion object : TlCombinator<RldpMessage>(
        RldpMessage::class,
        RldpMessageData::class to RldpMessageData,
        RldpQuery::class to RldpQuery,
        RldpAnswer::class to RldpAnswer,
    )
}
