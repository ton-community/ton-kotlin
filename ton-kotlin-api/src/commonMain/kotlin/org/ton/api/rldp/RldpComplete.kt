package org.ton.api.rldp

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.*

@Serializable
@SerialName("rldp.complete")
data class RldpComplete(
    override val transfer_id: BitString,
    override val part: Int,
) : RldpMessagePart {
    init {
        require(transfer_id.size == 256) { "Invalid transfer_id size; expected: 256, actual: ${transfer_id.size}" }
    }

    override fun tlCodec(): TlCodec<RldpComplete> = Companion

    companion object : TlConstructor<RldpComplete>(
        schema = "rldp.complete transfer_id:int256 part:int = rldp.MessagePart",
    ) {
        override fun encode(output: Output, value: RldpComplete) {
            output.writeInt256Tl(value.transfer_id)
            output.writeIntTl(value.part)
        }

        override fun decode(input: Input): RldpComplete {
            val transfer_id = input.readInt256Tl()
            val part = input.readIntTl()
            return RldpComplete(BitString(transfer_id), part)
        }
    }
}
