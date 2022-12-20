package org.ton.api.rldp

import kotlinx.serialization.Serializable
import org.ton.tl.Bits256
import org.ton.tl.TlCombinator
import org.ton.tl.TlObject

@Serializable
public sealed interface RldpMessagePart : TlObject<RldpMessagePart> {
    public val transfer_id: Bits256
    public val part: Int

    public companion object : TlCombinator<RldpMessagePart>(
        RldpMessagePart::class,
        RldpMessagePartData::class to RldpMessagePartData,
        RldpConfirm::class to RldpConfirm,
        RldpComplete::class to RldpComplete,
    )
}
