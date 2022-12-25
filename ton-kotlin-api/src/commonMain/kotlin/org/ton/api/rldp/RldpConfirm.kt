package org.ton.api.rldp

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.tl.*
import org.ton.tl.constructors.*

@Serializable
@SerialName("rldp.confirm")
public data class RldpConfirm(
    @SerialName("transfer_id")
    override val transferId: Bits256,
    override val part: Int,
    val seqno: Int
) : RldpMessagePart {
    override fun tlCodec(): TlCodec<RldpConfirm> = Companion

    public companion object : TlConstructor<RldpConfirm>(
        schema = "rldp.confirm transfer_id:int256 part:int seqno:int = rldp.MessagePart",
    ) {
        override fun encode(output: TlWriter, value: RldpConfirm) {
            output.writeBits256(value.transferId)
            output.writeInt(value.part)
            output.writeInt(value.seqno)
        }

        override fun decode(input: TlReader): RldpConfirm {
            val transfer_id = input.readBits256()
            val part = input.readInt()
            val seqno = input.readInt()
            return RldpConfirm(transfer_id, part, seqno)
        }
    }
}
