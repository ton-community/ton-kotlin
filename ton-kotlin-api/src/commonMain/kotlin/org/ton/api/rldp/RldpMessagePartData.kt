package org.ton.api.rldp

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.fec.FecType
import org.ton.crypto.encodeHex
import org.ton.tl.*
import org.ton.tl.constructors.*

@Serializable
@SerialName("rldp.messagePart")
public data class RldpMessagePartData(
    override val transfer_id: Bits256,
    val fec_type: FecType,
    override val part: Int,
    val total_size: Long,
    val seqno: Int,
    val data: ByteArray
) : RldpMessagePart {
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

    public companion object : TlConstructor<RldpMessagePartData>(
        schema = "rldp.messagePart transfer_id:int256 fec_type:fec.Type part:int total_size:long seqno:int data:bytes = rldp.MessagePart",
    ) {
        override fun encode(output: TlWriter, value: RldpMessagePartData) {
            output.writeBits256(value.transfer_id)
            output.write(FecType, value.fec_type)
            output.writeInt(value.part)
            output.writeLong(value.total_size)
            output.writeInt(value.seqno)
            output.writeBytes(value.data)
        }

        override fun decode(input: TlReader): RldpMessagePartData {
            val transfer_id = input.readBits256()
            val fec_type = input.read(FecType)
            val part = input.readInt()
            val total_size = input.readLong()
            val seqno = input.readInt()
            val data = input.readBytes()
            return RldpMessagePartData(transfer_id, fec_type, part, total_size, seqno, data)
        }
    }
}
