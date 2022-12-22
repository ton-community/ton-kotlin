package org.ton.api.rldp

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.tl.*
import org.ton.tl.constructors.*

@Serializable
@SerialName("rldp.complete")
public data class RldpComplete(
    @SerialName("transfer_id")
    override val transferId: Bits256,
    override val part: Int,
) : RldpMessagePart {

    override fun tlCodec(): TlCodec<RldpComplete> = Companion

    public companion object : TlConstructor<RldpComplete>(
        schema = "rldp.complete transfer_id:int256 part:int = rldp.MessagePart",
    ) {
        override fun encode(output: TlWriter, value: RldpComplete) {
            output.writeBits256(value.transferId)
            output.writeInt(value.part)
        }

        override fun decode(input: TlReader): RldpComplete {
            val transfer_id = input.readBits256()
            val part = input.readInt()
            return RldpComplete(transfer_id, part)
        }
    }
}
