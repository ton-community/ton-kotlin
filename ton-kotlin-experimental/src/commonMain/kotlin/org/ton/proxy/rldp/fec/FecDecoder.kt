package org.ton.proxy.rldp.fec

import org.ton.api.fec.FecType

interface FecDecoder {
    val fecType: FecType

    fun decode(seqno: Int, data: ByteArray, output: ByteArray): Boolean
}
