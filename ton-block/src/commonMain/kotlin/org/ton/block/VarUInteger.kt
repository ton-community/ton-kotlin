package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bigint.BigInt
import org.ton.bigint.BigIntSerializer
import org.ton.bigint.bitLength
import org.ton.cell.*
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor

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
