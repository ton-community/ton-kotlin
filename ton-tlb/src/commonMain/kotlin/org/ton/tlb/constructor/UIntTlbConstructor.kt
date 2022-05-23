package org.ton.tlb.constructor

import org.ton.bigint.BigInt
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor

open class UIntTlbConstructor(
    val length: Int
) : TlbConstructor<BigInt>(
    schema = "uint\$_ = uint;"
) {
    override fun encode(
        cellBuilder: CellBuilder,
        value: BigInt,
        param: Int,
        negativeParam: (Int) -> Unit
    ) = cellBuilder {
        storeUInt(value, length)
    }

    override fun decode(
        cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit
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

        fun <T : Number> number(encode: CellBuilder.(T) -> Unit, decode: CellSlice.() -> T) =
            object : TlbConstructor<T>("") {
                override fun encode(
                    cellBuilder: CellBuilder,
                    value: T,
                    param: Int,
                    negativeParam: (Int) -> Unit
                ) {
                    encode(cellBuilder, value)
                }

                override fun decode(
                    cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit
                ): T {
                    return decode(cellSlice)
                }
            }
    }
}
