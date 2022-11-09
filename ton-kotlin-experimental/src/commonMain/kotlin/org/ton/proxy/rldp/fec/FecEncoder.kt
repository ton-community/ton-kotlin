package org.ton.proxy.rldp.fec

import io.github.andreypfau.raptorq.RaptorQEncoder
import io.ktor.utils.io.bits.*
import org.ton.api.fec.FecRaptorQ
import org.ton.api.fec.FecType

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
            symbol_count = (totalSize + 768 - 1) / 768,
        ), packets
    )

    constructor(data: ByteArray) : this(data.size, RaptorQEncoder(data, 768, 5).encode())

    override fun encode(seqno: Int, output: ByteArray): Int {
        val packet = packets[seqno]
        return packet.useMemory(0, packet.size) {
            val newSeqno = it.loadIntAt(0)
            it.loadByteArray(4, output)
            newSeqno
        }
    }
}
