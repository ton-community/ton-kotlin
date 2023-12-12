package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bigint.BigInt
import org.ton.bigint.BigIntSerializer
import org.ton.bigint.bitLength
import org.ton.bigint.toBigInt
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbConstructor
import kotlin.jvm.JvmStatic

@SerialName("var_int")
@Serializable
public data class VarInteger(
    val len: Int,
    @Serializable(BigIntSerializer::class)
    val value: BigInt
) : Number() {
    @Deprecated("Use explicit constructor instead. In feature TLB classes will be auto-generated by TLB parser")
    public constructor(int: Int) : this(int.toBigInt().bitLength, int.toBigInt())

    @Deprecated("Use explicit constructor instead. In feature TLB classes will be auto-generated by TLB parser")
    public constructor(long: Long) : this(long.toBigInt().bitLength, long.toBigInt())

    @Deprecated("Use explicit constructor instead. In feature TLB classes will be auto-generated by TLB parser")
    public constructor(value: BigInt) : this(value.bitLength, value)

    @Deprecated("Use value.toByte() instead", replaceWith = ReplaceWith("value.toByte()"))
    override fun toByte(): Byte = value.toByte()

    @Deprecated("Use value.toChar() instead", replaceWith = ReplaceWith("value.toInt().toChar()"))
    override fun toChar(): Char = value.toInt().toChar()

    @Deprecated("Use value.toDouble() instead", replaceWith = ReplaceWith("value.toDouble()"))
    override fun toDouble(): Double = throw UnsupportedOperationException()

    @Deprecated("Use value.toFloat() instead", replaceWith = ReplaceWith("value.toFloat()"))
    override fun toFloat(): Float = throw UnsupportedOperationException()

    @Deprecated("Use value.toInt() instead", replaceWith = ReplaceWith("value.toInt()"))
    override fun toInt(): Int = value.toInt()

    @Deprecated("Use value.toLong() instead", replaceWith = ReplaceWith("value.toLong()"))
    override fun toLong(): Long = value.toLong()

    @Deprecated("Use value.toShort() instead", replaceWith = ReplaceWith("value.toShort()"))
    override fun toShort(): Short = value.toShort()

    public companion object {
        @JvmStatic
        public fun tlbCodec(n: Int): TlbConstructor<VarInteger> = VarIntegerTlbConstructor(n)
    }
}

private class VarIntegerTlbConstructor(
    val n: Int
) : TlbConstructor<VarInteger>(
    schema = "var_int\$_ {n:#} len:(#< n) value:(int (len * 8)) = VarInteger n;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder, value: VarInteger
    ) = cellBuilder {
        storeUIntLeq(value.len, n)
        storeInt(value.value, value.len * 8)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): VarInteger = cellSlice {
        val len = loadUIntLeq(n).toInt()
        val value = loadInt(len)
        VarInteger(len, value)
    }
}
