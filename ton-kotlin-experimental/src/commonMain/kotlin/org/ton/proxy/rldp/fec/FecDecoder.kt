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
    private val outputBuffer = ByteArray(fecType.data_size)
    private val inputBuffer = ByteArray(fecType.data_size + 4)
    private val engine = RaptorQDecoder(fecType.data_size, fecType.symbol_size)

    override fun decode(seqno: Int, data: ByteArray): ByteArray? {
        if (decoded) return null
        this.seqno = seqno
        val packet = buildPacket {
            writeInt(seqno)
            writeFully(data)
        }.readBytes()
        return if (engine.decode(packet, outputBuffer)) {
            decoded = true
            outputBuffer
        } else {
            null
        }
    }
}
