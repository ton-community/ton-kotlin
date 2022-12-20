@file:Suppress("OPT_IN_USAGE")

package org.ton.api.rldp

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.bitstring.BitString
import org.ton.tl.Bits256
import org.ton.tl.TlCombinator
import org.ton.tl.TlObject

@Polymorphic
@Serializable
@JsonClassDiscriminator("@type")
public sealed interface RldpMessage : TlObject<RldpMessage> {
    public val id: Bits256
    public val data: ByteArray

    public companion object : TlCombinator<RldpMessage>(
        RldpMessage::class,
        RldpMessageData::class to RldpMessageData,
        RldpQuery::class to RldpQuery,
        RldpAnswer::class to RldpAnswer,
    )
}
