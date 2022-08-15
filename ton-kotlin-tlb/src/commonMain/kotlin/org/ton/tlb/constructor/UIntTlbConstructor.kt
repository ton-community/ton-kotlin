package org.ton.tlb.constructor

import org.ton.bigint.BigInt
import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbConstructor

open class UIntTlbConstructor(
    val length: Int
) : TlbConstructor<BigInt>(
    schema = "uint\$_ = uint;",
    id = BitString.empty()
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: BigInt
    ) = cellBuilder {
        storeUInt(value, length)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): BigInt = cellSlice {
        loadUInt(length)
    }

    companion object {
        fun byte(length: Int = Byte.SIZE_BITS) =
            number(encode = { storeUInt(it, length) }, decode = { loadUInt(length).toByte() })

        fun short(length: Int = Short.SIZE_BITS) =
            number(encode = { storeUInt(it, length) }, decode = { loadUInt(length).toShort() })

        fun int(length: Int = Int.SIZE_BITS) =
            number(encode = { storeUInt(it, length) }, decode = { loadUInt(length).toInt() })

        fun long(length: Int = Long.SIZE_BITS) =
            number(encode = { storeUInt(it, length) }, decode = { loadUInt(length).toLong() })

        fun uint32() =
            number(encode = { storeUInt32(it) }, decode = { loadUInt32() })

        fun uint64() =
            number(encode = { storeUInt64(it) }, decode = { loadUInt64() })

        fun <T : Any> number(encode: CellBuilder.(T) -> Unit, decode: CellSlice.() -> T) =
            object : TlbConstructor<T>("") {
                override fun storeTlb(
                    cellBuilder: CellBuilder,
                    value: T
                ) {
                    encode(cellBuilder, value)
                }

                override fun loadTlb(
                    cellSlice: CellSlice
                ): T {
                    return decode(cellSlice)
                }
            }
    }
}

fun UInt.Companion.tlbConstructor() = UIntTlbConstructor.uint32()
fun ULong.Companion.tlbConstructor() = UIntTlbConstructor.uint64()
