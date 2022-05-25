package org.ton.block.tlb

import org.ton.bitstring.toBitString
import org.ton.block.Anycast
import org.ton.block.Maybe
import org.ton.block.MsgAddressInt
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.*

fun MsgAddressInt.Companion.tlbCodec(): TlbCodec<MsgAddressInt> = MsgAddressIntTlbCombinator()

private class MsgAddressIntTlbCombinator : TlbCombinator<MsgAddressInt>() {
    private val addrStdConstructor by lazy {
        AddrStdTlbConstructor()
    }
    private val addrVarConstructor by lazy {
        AddrVarTlbConstructor()
    }

    override val constructors: List<TlbConstructor<out MsgAddressInt>> by lazy {
        listOf(addrStdConstructor, addrVarConstructor)
    }

    override fun getConstructor(value: MsgAddressInt): TlbConstructor<out MsgAddressInt> = when (value) {
        is MsgAddressInt.AddrStd -> addrStdConstructor
        is MsgAddressInt.AddrVar -> addrVarConstructor
    }

    private class AddrStdTlbConstructor : TlbConstructor<MsgAddressInt.AddrStd>(
        schema = "addr_std\$10 anycast:(Maybe Anycast) workchain_id:int8 address:bits256 = MsgAddressInt;"
    ) {
        private val maybeAnycastCodec by lazy {
            Maybe.tlbCodec(Anycast.tlbCodec())
        }

        override fun storeTlb(
            cellBuilder: CellBuilder,
            value: MsgAddressInt.AddrStd
        ) = cellBuilder {
            storeTlb(maybeAnycastCodec, value.anycast)
            storeInt(value.workchainId, 8)
            storeBits(value.address.toBitString())
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): MsgAddressInt.AddrStd = cellSlice {
            val anycast = loadTlb(maybeAnycastCodec)
            val workchainId = loadInt(8).toInt()
            val address = loadBitString(256).toByteArray()
            MsgAddressInt.AddrStd(anycast, workchainId, address)
        }
    }

    private class AddrVarTlbConstructor : TlbConstructor<MsgAddressInt.AddrVar>(
        schema = "addr_var\$11 anycast:(Maybe Anycast) addr_len:(## 9) workchain_id:int32 address:(bits addr_len) = MsgAddressInt;"
    ) {
        private val maybeAnycastCodec by lazy {
            Maybe.tlbCodec(Anycast.tlbCodec())
        }

        override fun storeTlb(
            cellBuilder: CellBuilder,
            value: MsgAddressInt.AddrVar
        ) = cellBuilder {
            storeTlb(maybeAnycastCodec, value.anycast)
            storeUInt(value.addrLen, 9)
            storeInt(value.workchainId, 32)
            storeBits(value.address)
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): MsgAddressInt.AddrVar = cellSlice {
            val anycast = loadTlb(maybeAnycastCodec)
            val addrLen = loadUInt(9).toInt()
            val workchainId = loadInt(32).toInt()
            val address = loadBitString(addrLen)
            MsgAddressInt.AddrVar(anycast, addrLen, workchainId, address)
        }
    }
}
