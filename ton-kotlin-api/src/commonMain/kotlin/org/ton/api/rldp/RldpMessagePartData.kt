package org.ton.api.rldp

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.fec.FecType
import org.ton.bitstring.Bits256
import org.ton.crypto.encodeHex
import org.ton.tl.*

@Serializable
@SerialName("rldp.messagePart")
public data class RldpMessagePartData(
    override val transferId: Bits256,
    @SerialName("fec_type")
    val fecType: FecType,
    override val part: Int,
    @SerialName("total_size")
    val totalSize: Long,
    val seqno: Int,
    val data: ByteArray
) : RldpMessagePart {
    override fun tlCodec(): TlCodec<RldpMessagePartData> = Companion

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RldpMessagePartData) return false
        if (transferId != other.transferId) return false
        if (part != other.part) return false
        if (seqno != other.seqno) return false
        if (fecType != other.fecType) return false
        if (totalSize != other.totalSize) return false
        if (!data.contentEquals(other.data)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = transferId.hashCode()
        result = 31 * result + fecType.hashCode()
        result = 31 * result + part
        result = 31 * result + totalSize.hashCode()
        result = 31 * result + seqno
        result = 31 * result + data.contentHashCode()
        return result
    }

    override fun toString(): String {
        return "RldpMessagePartData(transfer_id=$transferId, fec_type=$fecType, part=$part, total_size=$totalSize, seqno=$seqno, data=${data.encodeHex()})"
    }

    public companion object : TlConstructor<RldpMessagePartData>(
        schema = "rldp.messagePart transfer_id:int256 fec_type:fec.Type part:int total_size:long seqno:int data:bytes = rldp.MessagePart",
    ) {
        override fun encode(output: TlWriter, value: RldpMessagePartData) {
            output.writeBits256(value.transferId)
            output.write(FecType, value.fecType)
            output.writeInt(value.part)
            output.writeLong(value.totalSize)
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
