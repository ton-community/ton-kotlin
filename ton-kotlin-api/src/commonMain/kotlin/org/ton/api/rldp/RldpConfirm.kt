package org.ton.api.rldp

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.*

@Serializable
@SerialName("rldp.confirm")
data class RldpConfirm(
    override val transfer_id: BitString,
    override val part: Int,
    val seqno: Int
) : RldpMessagePart {
    init {
        require(transfer_id.size == 256) { "Invalid transfer_id size; expected: 256, actual: ${transfer_id.size}" }
    }

    override fun tlCodec(): TlCodec< RldpConfirm> = Companion

    companion object : TlConstructor<RldpConfirm>(
        schema = "rldp.confirm transfer_id:int256 part:int seqno:int = rldp.MessagePart",
    ) {
        override fun encode(output: Output, value: RldpConfirm) {
            output.writeInt256Tl(value.transfer_id)
            output.writeIntTl(value.part)
            output.writeIntTl(value.seqno)
        }

        override fun decode(input: Input): RldpConfirm {
            val transfer_id = input.readInt256Tl()
            val part = input.readIntTl()
            val seqno = input.readIntTl()
            return RldpConfirm(BitString(transfer_id), part, seqno)
        }
    }
}
