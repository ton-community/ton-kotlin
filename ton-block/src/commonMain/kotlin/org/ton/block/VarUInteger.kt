package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bigint.BigInt
import org.ton.bigint.BigIntSerializer
import org.ton.bigint.bitLength

@SerialName("var_uint")
@Serializable
data class VarUInteger(
        val len: Int,
        @Serializable(BigIntSerializer::class)
        val value: BigInt
) : Number() {
    constructor(value: BigInt) : this(value.bitLength, value)

    override fun toByte(): Byte = value.toByte()
    override fun toChar(): Char = value.toChar()
    override fun toDouble(): Double = value.toDouble()
    override fun toFloat(): Float = value.toFloat()
    override fun toInt(): Int = value.toInt()
    override fun toLong(): Long = value.toLong()
    override fun toShort(): Short = value.toShort()
}