package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.bitstring.toBitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@SerialName("addr_var")
@Serializable
data class MsgAddressIntVar(
    val anycast: Maybe<Anycast>,
    val addrLen: Int,
    override val workchainId: Int,
    val address: BitString
) : MsgAddressInt {
    init {
        require(address.size == addrLen) { "address.size expected: $addrLen actual: ${address.size}" }
    }

    constructor(workchainId: Int, address: ByteArray) : this(null, workchainId, address)
    constructor(workchainId: Int, address: BitString) : this(null, workchainId, address)
    constructor(anycast: Anycast?, workchainId: Int, address: BitString) : this(
        anycast.toMaybe(),
        address.size,
        workchainId,
        address
    )

    constructor(anycast: Anycast?, workchainId: Int, address: ByteArray) : this(
        anycast.toMaybe(),
        address.size,
        workchainId,
        address.toBitString()
    )

    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<MsgAddressIntVar> = AddrVarTlbConstructor()
    }
}

internal class AddrVarTlbConstructor : TlbConstructor<MsgAddressIntVar>(
    schema = "addr_var\$11 anycast:(Maybe Anycast) addr_len:(## 9) workchain_id:int32 address:(bits addr_len) = MsgAddressInt;"
) {
    private val maybeAnycastCodec by lazy {
        Maybe.tlbCodec(Anycast.tlbCodec())
    }

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: MsgAddressIntVar
    ) = cellBuilder {
        storeTlb(maybeAnycastCodec, value.anycast)
        storeUInt(value.addrLen, 9)
        storeInt(value.workchainId, 32)
        storeBits(value.address)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): MsgAddressIntVar = cellSlice {
        val anycast = loadTlb(maybeAnycastCodec)
        val addrLen = loadUInt(9).toInt()
        val workchainId = loadInt(32).toInt()
        val address = loadBitString(addrLen)
        MsgAddressIntVar(anycast, addrLen, workchainId, address)
    }
}
