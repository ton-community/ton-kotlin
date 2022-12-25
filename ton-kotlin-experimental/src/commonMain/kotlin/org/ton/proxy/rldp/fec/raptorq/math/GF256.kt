package org.ton.proxy.rldp.fec.raptorq.math

import kotlin.experimental.xor

class GF256(
    val data: ByteArray
) {
    constructor(size: Int) : this(ByteArray(size))

    fun add(other: GF256) {
        for (i in data.indices) {
            data[i] = (data[i] xor other.data[i])
        }
    }

    fun mul(x: Octet) {
        for (i in data.indices) {
            data[i] = (data[i].toOctet() * x).toByte()
        }
    }

    fun addMul(other: GF256, x: Octet) {
        for (i in data.indices) {
            data[i] = (data[i].toOctet() + (x * other.data[i].toOctet())).toByte()
        }
    }
}
