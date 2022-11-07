package org.ton.proxy.rldp.fec

import io.github.andreypfau.raptorq.RaptorQEncoder
import io.ktor.utils.io.core.*
import org.ton.api.fec.FecRaptorQ
import org.ton.api.fec.FecType
import org.ton.crypto.encodeHex

interface FecEncoder {
    val fecType: FecType
    fun encode(seqno: Int, output: ByteArray): Int
}

class RaptorQFecEncoder(
    override val fecType: FecRaptorQ,
    val packets: Array<ByteArray>
) : FecEncoder {
    constructor(totalSize: Int, packets: Array<ByteArray>) : this(
        FecRaptorQ(
            data_size = totalSize,
            symbol_size = 768,
            symbol_count = packets.size,
        ), packets
    )

    constructor(data: ByteArray) : this(data.size, RaptorQEncoder(data, 768, 1).encode())

    override fun encode(seqno: Int, output: ByteArray): Int {
        val packet = ByteReadPacket(packets[seqno])
        packet.discard(2)
        val newSeqno = packet.readShort().toInt()
        packet.readFully(output)
        return newSeqno
    }
}
