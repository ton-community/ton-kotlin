package org.ton.tlb.constructor

import org.ton.bigint.BigInt
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor

class IntTlbConstructor(
    val length: Int
) : TlbConstructor<BigInt>(
    schema = "int\$_ = int;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: BigInt
    ) = cellBuilder {
        storeInt(value, length)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): BigInt = cellSlice {
        loadInt(length)
    }

    companion object {
        fun byte(length: Int = Byte.SIZE_BITS) =
            number(encode = { storeInt(it, length) }, decode = { loadInt(length).toByte() })

        fun short(length: Int = Short.SIZE_BITS) =
            number(encode = { storeInt(it, length) }, decode = { loadInt(length).toShort() })

        fun int(length: Int = Int.SIZE_BITS) =
            number(encode = { storeInt(it, length) }, decode = { loadInt(length).toInt() })

        fun long(length: Int = Long.SIZE_BITS) =
            number(encode = { storeInt(it, length) }, decode = { loadInt(length).toLong() })

        fun <T : Number> number(encode: CellBuilder.(T) -> Unit, decode: CellSlice.() -> T) =
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

