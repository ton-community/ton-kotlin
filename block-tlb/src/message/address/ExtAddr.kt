package org.ton.kotlin.message.address

import org.ton.kotlin.bitstring.BitString
import org.ton.kotlin.bitstring.toBitString
import org.ton.kotlin.cell.CellBuilder
import org.ton.kotlin.cell.CellContext
import org.ton.kotlin.cell.CellSlice
import org.ton.kotlin.cell.serialization.CellSerializer

/**
 * External address.
 *
 * ```tlb
 * addr_extern$01 len:(## 9) external_address:(bits len) = MsgAddressExt;
 * ```
 */
public data class ExtAddr(
    val address: BitString
) : MsgAddress {
    public constructor(externalAddress: ByteArray) : this(externalAddress.toBitString())

    override fun toString(): String = "ExtAddr($address)"

    public companion object : CellSerializer<ExtAddr?> by AddrExternSerializer
}

private object AddrExternSerializer : CellSerializer<ExtAddr?> {
    override fun load(
        slice: CellSlice,
        context: CellContext
    ): ExtAddr? {
        return when (val tag = slice.loadUInt(2).toInt()) {
            0b00 -> { // addr_none$00
                null
            }

            0b01 -> { // addr_extern$01
                val len = slice.loadUInt(9).toInt()
                val addr = slice.loadBitString(len)
                ExtAddr(addr)
            }

            else -> throw IllegalStateException("Unknown extern addr: $tag")
        }
    }

    override fun store(
        builder: CellBuilder,
        value: ExtAddr?,
        context: CellContext
    ) {
        if (value == null) { // addr_none$00
            builder.storeUInt(0b00u, 2)
        } else { // addr_extern$01
            builder.storeUInt(0b01u, 2)
            builder.storeUInt(value.address.size.toUInt(), 9)
            builder.storeBitString(value.address)
        }
    }
}