package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bigint.BigInt
import org.ton.bigint.BigIntSerializer
import org.ton.bigint.bitLength

@SerialName("var_int")
@Serializable
data class VarInteger(
    val len: Int,
    @Serializable(BigIntSerializer::class)
    val value: BigInt
) : Number() {
    constructor(byte: Byte) : this(BigInt(byte))
    constructor(short: Short) : this(BigInt(short))
    constructor(int: Int) : this(BigInt(int))
    constructor(long: Long) : this(BigInt(long))
    constructor(value: BigInt) : this(value.bitLength, value)

    override fun toByte(): Byte = value.toByte()
    override fun toChar(): Char = value.toChar()
    override fun toDouble(): Double = throw UnsupportedOperationException()
    override fun toFloat(): Float = throw UnsupportedOperationException()
    override fun toInt(): Int = value.toInt()
    override fun toLong(): Long = value.toLong()
    override fun toShort(): Short = value.toShort()
}
