package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.bitstring.toBitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb
import kotlin.jvm.JvmStatic

@Serializable
@SerialName("addr_var")
data class AddrVar(
    val anycast: Maybe<Anycast>,
    val addr_len: Int,
    override val workchain_id: Int,
    val address: BitString
) : MsgAddressInt {
    init {
        require(address.size == addr_len) { "required: address.size == addr_len, actual: ${address.size}" }
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

    override fun toString(): String = buildString {
        append("(addr_var\n")
        append("anycast:")
        append(anycast)
        append(" addr_len:")
        append(addr_len)
        append(" workchain_id:")
        append(workchain_id)
        append(" address:")
        append(address)
        append(")")
    }

    companion object : TlbCodec<AddrVar> by AddrVarTlbConstructor {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<AddrVar> = AddrVarTlbConstructor
    }
}

private object AddrVarTlbConstructor : TlbConstructor<AddrVar>(
    schema = "addr_var\$11 anycast:(Maybe Anycast) addr_len:(## 9) workchain_id:int32 address:(bits addr_len) = MsgAddressInt;"
) {
    private val MaybeAnycast = Maybe(Anycast)

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: AddrVar
    ) = cellBuilder {
        storeTlb(MaybeAnycast, value.anycast)
        storeUInt(value.addr_len, 9)
        storeInt(value.workchain_id, 32)
        storeBits(value.address)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): AddrVar = cellSlice {
        val anycast = loadTlb(MaybeAnycast)
        val addrLen = loadUInt(9).toInt()
        val workchainId = loadInt(32).toInt()
        val address = loadBits(addrLen)
        AddrVar(anycast, addrLen, workchainId, address)
    }
}
