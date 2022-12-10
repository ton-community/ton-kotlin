package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bigint.*
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import kotlin.jvm.JvmStatic

@SerialName("var_uint")
@Serializable
data class VarUInteger(
    val len: Int,
    @Serializable(BigIntSerializer::class)
    val value: BigInt
) {
    constructor(byte: Byte) : this(BigInt(byte))
    constructor(short: Short) : this(BigInt(short))
    constructor(int: Int) : this(BigInt(int))
    constructor(long: Long) : this(BigInt(long))
    constructor(value: BigInt) : this(
        len = value.bitLength / Byte.SIZE_BITS + if (value.bitLength % Byte.SIZE_BITS == 0) 0 else 1,
        value = value
    )

    fun toByte(): Byte = value.toByte()
    fun toChar(): Char = value.toChar()
    fun toDouble(): Double = throw UnsupportedOperationException()
    fun toFloat(): Float = throw UnsupportedOperationException()
    fun toInt(): Int = value.toInt()
    fun toLong(): Long = value.toLong()
    fun toShort(): Short = value.toShort()

    operator fun plus(other: VarUInteger): VarUInteger {
        val result = value + other.value
        val maxLen = maxOf(len, other.len)
        val actualLen = result.bitLength
        val length = if (actualLen <= maxLen) maxLen else throw NumberFormatException("Integer overflow")
        return VarUInteger(length, result)
    }

    operator fun minus(other: VarUInteger): VarUInteger {
        val result = value - other.value
        if (result < 0L) throw NumberFormatException("Integer underflow")
        val len = maxOf(len, other.len)
        return VarUInteger(len, result)
    }

    operator fun times(other: VarUInteger): VarUInteger {
        val result = value * other.value
        val maxLen = maxOf(len, other.len)
        val actualLen = result.bitLength
        val len = if (actualLen <= maxLen) maxLen else throw NumberFormatException("Integer overflow")
        return VarUInteger(len, result)
    }

    operator fun div(other: VarUInteger): VarUInteger {
        val result = value / other.value
        val maxLen = maxOf(len, other.len)
        val actualLen = result.bitLength
        val len = if (actualLen <= maxLen) maxLen else throw NumberFormatException("Integer overflow")
        return VarUInteger(len, result)
    }

    operator fun rem(other: VarUInteger): VarUInteger {
        val result = value % other.value
        val maxLen = maxOf(len, other.len)
        val actualLen = result.bitLength
        val len = if (actualLen <= maxLen) maxLen else throw NumberFormatException("Integer overflow")
        return VarUInteger(len, result)
    }

    operator fun inc(): VarUInteger {
        val result = value + 1.toBigInt()
        val actualLen = result.bitLength
        val length = if (actualLen < len) len else throw NumberFormatException("Integer overflow")
        return VarUInteger(length, result)
    }

    operator fun dec(): VarUInteger {
        val result = value - 1.toBigInt()
        if (result < 0.toBigInt()) throw NumberFormatException("Integer overflow")
        return VarUInteger(len, result)
    }

    override fun toString(): String = value.toString()

    companion object {
        @JvmStatic
        fun tlbCodec(n: Int): TlbCodec<VarUInteger> = VarUIntegerTlbConstructor(n)
    }

    private class VarUIntegerTlbConstructor(
        val n: Int
    ) : TlbConstructor<VarUInteger>(
        schema = "var_uint\$_ {n:#} len:(#< n) value:(uint (len * 8)) = VarUInteger n;"
    ) {
        override fun storeTlb(
            cellBuilder: CellBuilder, value: VarUInteger
        ) = cellBuilder {
            storeUIntLes(value.len, n)
            storeUInt(value.value, value.len * 8)
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): VarUInteger = cellSlice {
            val len = loadUIntLes(n).toInt()
            val value = loadUInt(len * 8)
            VarUInteger(len, value)
        }
    }
}
