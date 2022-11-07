package org.ton.proxy.rldp.fec

import io.github.andreypfau.raptorq.RaptorQDecoder
import io.ktor.utils.io.core.*
import org.ton.api.fec.FecRaptorQ
import org.ton.api.fec.FecType

interface FecDecoder {
    val seqno: Int

    val fecType: FecType

    fun decode(seqno: Int, data: ByteArray): ByteArray?
}

class RaptorQFecDecoder(
    override val fecType: FecRaptorQ
) : FecDecoder {
    override var seqno: Int = 0
        private set

    private var decoded = false
    private val buffer = ByteArray(fecType.data_size)
    private val engine = RaptorQDecoder(fecType.data_size, fecType.symbol_size)

    override fun decode(seqno: Int, data: ByteArray): ByteArray? {
        if (decoded) {
            return buffer
        }
        this.seqno = seqno
        val packet = buildPacket {
            writeShort(0)
            writeShort(seqno.toShort())
            writeFully(data)
        }.readBytes()
        return if(engine.decode(packet, buffer)) {
            decoded = true
            buffer
        } else {
            null
        }
    }
}
