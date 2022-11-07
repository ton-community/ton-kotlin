package org.ton.api.rldp

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.Int256TlConstructor
import org.ton.tl.constructors.IntTlConstructor
import org.ton.tl.constructors.writeInt256Tl
import org.ton.tl.constructors.writeIntTl

@Serializable
@SerialName("rldp.confirm")
data class RldpConfirm(
    val transfer_id: BitString,
    val part: Int,
    val seqno: Int
) : RldpMessagePart {
    init {
        require(transfer_id.size == 256) { "Invalid transfer_id size; expected: 256, actual: ${transfer_id.size}" }
    }

    override fun tlCodec(): TlCodec< RldpConfirm> = Companion

    companion object : TlConstructor<RldpConfirm>(
        type = RldpConfirm::class,
        schema = "rldp.confirm transfer_id:int256 part:int seqno:int = rldp.MessagePart",
        fields = listOf(
            Int256TlConstructor,
            IntTlConstructor,
            IntTlConstructor
        )
    ) {
        override fun encode(output: Output, value: RldpConfirm) {
            output.writeInt256Tl(value.transfer_id)
            output.writeIntTl(value.part)
            output.writeIntTl(value.seqno)
        }

        override fun decode(values: Iterator<*>): RldpConfirm {
            val transfer_id = BitString(values.next() as ByteArray)
            val part = values.next() as Int
            val seqno = values.next() as Int
            return RldpConfirm(transfer_id, part, seqno)
        }
    }
}
