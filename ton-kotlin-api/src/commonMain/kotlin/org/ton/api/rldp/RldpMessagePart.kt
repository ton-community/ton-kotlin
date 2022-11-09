package org.ton.api.rldp

import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.tl.TlCombinator
import org.ton.tl.TlObject

@Serializable
sealed interface RldpMessagePart : TlObject<RldpMessagePart> {
    val transfer_id: BitString
    val part: Int

    companion object : TlCombinator<RldpMessagePart>(
        RldpMessagePartData,
        RldpConfirm,
        RldpComplete,
    )
}
