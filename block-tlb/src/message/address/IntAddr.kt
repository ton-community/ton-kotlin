@file:Suppress("NOTHING_TO_INLINE", "PropertyName")

package org.ton.kotlin.message.address

import org.ton.kotlin.bitstring.BitString
import org.ton.kotlin.cell.CellBuilder
import org.ton.kotlin.cell.CellContext
import org.ton.kotlin.cell.CellSlice
import org.ton.kotlin.cell.serialization.CellSerializer
import kotlin.contracts.contract
import kotlin.jvm.JvmStatic

public inline fun MsgAddressInt(address: String): IntAddr = IntAddr.parse(address)

public sealed interface IntAddr : MsgAddress {
    public val workchain: Int
    public val address: BitString

    public fun toAddrStd(): StdAddr

    public fun rewriteAnycast(): IntAddr

    public companion object : CellSerializer<IntAddr> by IntAddrSerializer {

        @JvmStatic
        public fun toString(
            address: IntAddr,
            userFriendly: Boolean = true,
            urlSafe: Boolean = true,
            testOnly: Boolean = false,
            bounceable: Boolean = true
        ): String {
            checkAddressStd(address)
            return StdAddr.Companion.toString(address, userFriendly, urlSafe, testOnly, bounceable)
        }

        @JvmStatic
        public fun parse(address: String): IntAddr = StdAddr.Companion.parse(address)

        @JvmStatic
        public fun parseRaw(address: String): IntAddr = StdAddr.Companion.parseRaw(address)

        @JvmStatic
        public fun parseUserFriendly(address: String): IntAddr = StdAddr.Companion.parseUserFriendly(address)

        private fun checkAddressStd(value: IntAddr) {
            contract {
                returns() implies (value is StdAddr)
            }
            require(value is StdAddr) {
                "expected class: ${StdAddr::class} actual: ${value::class}"
            }
        }
    }
}

private object IntAddrSerializer : CellSerializer<IntAddr> {
    override fun load(slice: CellSlice, context: CellContext): IntAddr {
        return when (val tag = slice.loadUInt(3).toInt()) {
            0b100 -> { // addr_std$10, anycast=nothing$0
                val workchain = slice.loadInt(8)
                val address = slice.loadBitString(256)
                StdAddr(null, workchain, address)
            }

            0b101 -> { // addr_std$10, anycast=just$1
                val anycast = slice.load(Anycast)
                val workchain = slice.loadInt(8)
                val address = slice.loadBitString(256)
                StdAddr(anycast, workchain, address)
            }

            0b110 -> { // addr_var$11, anycast=nothing$0
                val len = slice.loadUInt(9).toInt()
                val workchain = slice.loadInt(32)
                val address = slice.loadBitString(len)
                VarAddr(null, workchain, address)
            }

            0b111 -> { // addr_var$11, anycast=just$1
                val anycast = slice.load(Anycast)
                val len = slice.loadUInt(9).toInt()
                val workchain = slice.loadInt(32)
                val address = slice.loadBitString(len)
                VarAddr(anycast, workchain, address)
            }

            else -> throw IllegalArgumentException("unknown address type: ${tag.toString(2)}")
        }
    }

    override fun store(
        builder: CellBuilder,
        value: IntAddr,
        context: CellContext
    ) {
        when (value) {
            is StdAddr -> {
                if (value.anycast == null) {
                    // addr_std$10, anycast=nothing$0
                    builder.storeUInt(0b100u, 3)
                } else {
                    // addr_std$10, anycast=just$1
                    builder.storeUInt(0b101u, 3)
                }
                builder.storeInt(value.workchain, 8)
                builder.storeBitString(value.address)
            }

            is VarAddr -> {
                if (value.anycast == null) {
                    // addr_var$11, anycast=nothing$0
                    builder.storeUInt(0b110u, 3)
                } else {
                    // addr_var$11, anycast=just$1
                    builder.storeUInt(0b111u, 3)
                }
                builder.storeInt(value.workchain, 32)
                builder.storeBitString(value.address)
            }
        }
    }
}