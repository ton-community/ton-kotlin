package org.ton.tlb.constructor

import org.ton.bigint.BigInt
import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbConstructor

public open class UIntTlbConstructor(
    public val length: Int
) : TlbConstructor<BigInt>(
    schema = "uint\$_ = uint;",
    id = BitString.empty()
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: BigInt
    ) {
        cellBuilder.storeUInt(value, length)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): BigInt = cellSlice {
        loadUInt(length)
    }

    public companion object {
        public fun byte(length: Int = Byte.SIZE_BITS): TlbConstructor<Byte> =
            number(encode = { storeUInt(it, length) }, decode = { loadUInt(length).toByte() })

        public fun short(length: Int = Short.SIZE_BITS): TlbConstructor<Short> =
            number(encode = { storeUInt(it, length) }, decode = { loadUInt(length).toShort() })

        public fun int(length: Int = Int.SIZE_BITS): TlbConstructor<Int> =
            number(encode = { storeUInt(it, length) }, decode = { loadUInt(length).toInt() })

        public fun long(length: Int = Long.SIZE_BITS): TlbConstructor<Long> =
            number(encode = { storeUInt(it, length) }, decode = { loadUInt(length).toLong() })

        private fun <T : Any> number(encode: CellBuilder.(T) -> Unit, decode: CellSlice.() -> T) =
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

public fun UInt.Companion.tlbConstructor(): TlbConstructor<Int> = UIntTlbConstructor.int()
public fun ULong.Companion.tlbConstructor(): TlbConstructor<Long> = UIntTlbConstructor.long()
