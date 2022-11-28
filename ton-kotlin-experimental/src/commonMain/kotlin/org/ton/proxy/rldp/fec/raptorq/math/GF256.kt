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

    fun mul(x: Byte) {
        for (i in data.indices) {
            data[i] = (data[i].toOctet() * x.toOctet()).toByte()
        }
    }

    fun addMul(other: GF256, x: Byte) {
        for (i in data.indices) {
            data[i] = (data[i].toOctet() + (x.toOctet() * other.data[i].toOctet())).toByte()
        }
    }
}
