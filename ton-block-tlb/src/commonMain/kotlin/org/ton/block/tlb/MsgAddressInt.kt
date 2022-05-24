package org.ton.block.tlb

import org.ton.bitstring.toBitString
import org.ton.block.Anycast
import org.ton.block.Maybe
import org.ton.block.MsgAddressInt
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.*

fun MsgAddressInt.Companion.tlbCodec(): TlbCodec<MsgAddressInt> = MsgAddressIntTlbCombinator()

private class MsgAddressIntTlbCombinator : TlbCombinator<MsgAddressInt>(
    AddrStdTlbConstructor, AddrVarTlbConstructor
) {
    override fun getConstructor(value: MsgAddressInt): TlbConstructor<out MsgAddressInt> = when (value) {
        is MsgAddressInt.AddrStd -> AddrStdTlbConstructor
        is MsgAddressInt.AddrVar -> AddrVarTlbConstructor
    }

    object AddrStdTlbConstructor : TlbConstructor<MsgAddressInt.AddrStd>(
        schema = "addr_std\$10 anycast:(Maybe Anycast) workchain_id:int8 address:bits256 = MsgAddressInt;"
    ) {
        private val maybeAnycastCodec = Maybe.tlbCodec(Anycast.tlbCodec())

        override fun encode(
            cellBuilder: CellBuilder,
            value: MsgAddressInt.AddrStd,
            param: Int,
            negativeParam: (Int) -> Unit
        ) = cellBuilder {
            storeTlb(value.anycast, maybeAnycastCodec)
            storeInt(value.workchainId, 8)
            storeBits(value.address.toBitString())
        }

        override fun decode(
            cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit
        ): MsgAddressInt.AddrStd = cellSlice {
            val anycast = loadTlb(maybeAnycastCodec)
            val workchainId = loadInt(8).toInt()
            val address = loadBitString(256).toByteArray()
            MsgAddressInt.AddrStd(anycast, workchainId, address)
        }
    }

    object AddrVarTlbConstructor : TlbConstructor<MsgAddressInt.AddrVar>(
        schema = "addr_var\$11 anycast:(Maybe Anycast) addr_len:(## 9) workchain_id:int32 address:(bits addr_len) = MsgAddressInt;"
    ) {
        private val maybeAnycastCodec = Maybe.tlbCodec(Anycast.tlbCodec())

        override fun encode(
            cellBuilder: CellBuilder,
            value: MsgAddressInt.AddrVar,
            param: Int,
            negativeParam: (Int) -> Unit
        ) = cellBuilder {
            storeTlb(value.anycast, maybeAnycastCodec)
            storeUInt(value.addrLen, 9)
            storeInt(value.workchainId, 32)
            storeBits(value.address)
        }

        override fun decode(
            cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit
        ): MsgAddressInt.AddrVar = cellSlice {
            val anycast = loadTlb(maybeAnycastCodec)
            val addrLen = loadUInt(9).toInt()
            val workchainId = loadInt(32).toInt()
            val address = loadBitString(addrLen)
            MsgAddressInt.AddrVar(anycast, addrLen, workchainId, address)
        }
    }
}
