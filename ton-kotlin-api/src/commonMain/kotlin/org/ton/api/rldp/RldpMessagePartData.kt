package org.ton.api.rldp

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.fec.FecType
import org.ton.bitstring.BitString
import org.ton.crypto.encodeHex
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.*
import org.ton.tl.writeTl

@Serializable
@SerialName("rldp.messagePart")
data class RldpMessagePartData(
    override val transfer_id: BitString,
    val fec_type: FecType,
    override val part: Int,
    val total_size: Long,
    val seqno: Int,
    val data: ByteArray
) : RldpMessagePart {
    init {
        require(transfer_id.size == 256) { "Invalid transfer_id size; expected: 256, actual: ${transfer_id.size}" }
    }

    override fun tlCodec(): TlCodec<RldpMessagePartData> = Companion

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RldpMessagePartData) return false
        if (transfer_id != other.transfer_id) return false
        if (part != other.part) return false
        if (seqno != other.seqno) return false
        if (fec_type != other.fec_type) return false
        if (total_size != other.total_size) return false
        if (!data.contentEquals(other.data)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = transfer_id.hashCode()
        result = 31 * result + fec_type.hashCode()
        result = 31 * result + part
        result = 31 * result + total_size.hashCode()
        result = 31 * result + seqno
        result = 31 * result + data.contentHashCode()
        return result
    }

    override fun toString(): String {
        return "RldpMessagePartData(transfer_id=$transfer_id, fec_type=$fec_type, part=$part, total_size=$total_size, seqno=$seqno, data=${data.encodeHex()})"
    }

    companion object : TlConstructor<RldpMessagePartData>(
        type = RldpMessagePartData::class,
        schema = "rldp.messagePart transfer_id:int256 fec_type:fec.Type part:int total_size:long seqno:int data:bytes = rldp.MessagePart",
        fields = listOf(
            Int256TlConstructor,
            FecType,
            IntTlConstructor,
            LongTlConstructor,
            IntTlConstructor,
            BytesTlConstructor
        )
    ) {
        override fun encode(output: Output, value: RldpMessagePartData) {
            output.writeInt256Tl(value.transfer_id)
            output.writeTl(FecType, value.fec_type)
            output.writeIntTl(value.part)
            output.writeLongTl(value.total_size)
            output.writeIntTl(value.seqno)
            output.writeBytesTl(value.data)
        }

        override fun decode(values: Iterator<*>): RldpMessagePartData {
            val transfer_id = BitString(values.next() as ByteArray)
            val fec_type = values.next() as FecType
            val part = values.next() as Int
            val total_size = values.next() as Long
            val seqno = values.next() as Int
            val data = values.next() as ByteArray
            return RldpMessagePartData(transfer_id, fec_type, part, total_size, seqno, data)
        }
    }
}
