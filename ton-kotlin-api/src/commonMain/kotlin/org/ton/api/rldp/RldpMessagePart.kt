package org.ton.api.rldp

import kotlinx.serialization.Serializable
import org.ton.tl.TlCombinator
import org.ton.tl.TlObject

@Serializable
sealed interface RldpMessagePart : TlObject<RldpMessagePart> {
    companion object : TlCombinator<RldpMessagePart>(
        RldpMessagePartData,
        RldpConfirm,
        RldpComplete,
    )
}
