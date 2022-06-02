@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.bitstring.BitString
import org.ton.bitstring.toBitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor

@Suppress("NOTHING_TO_INLINE")
inline fun MsgAddressExt(externalAddress: BitString? = null): MsgAddressExt = MsgAddressExt.of(externalAddress)

@JsonClassDiscriminator("@type")
@Serializable
sealed interface MsgAddressExt : MsgAddress {
    companion object {
        @JvmStatic
        fun of(externalAddress: BitString? = null): MsgAddressExt {
            return if (externalAddress.isNullOrEmpty()) {
                MsgAddressExtNone
            } else {
                MsgAddressExtOrdinary(externalAddress)
            }
        }

        @JvmStatic
        fun of(externalAddress: ByteArray): MsgAddressExt = MsgAddressExtOrdinary(externalAddress)

        @JvmStatic
        fun tlbCodec(): TlbCombinator<MsgAddressExt> = MsgAddressExtTlbCombinator
    }

    private object MsgAddressExtTlbCombinator : TlbCombinator<MsgAddressExt>() {
        private val addrNoneConstructor by lazy { MsgAddressExtNone.tlbCodec() }
        private val addrExternConstructor by lazy { MsgAddressExtOrdinary.tlbCodec() }

        override val constructors: List<TlbConstructor<out MsgAddressExt>> by lazy {
            listOf(addrNoneConstructor, addrExternConstructor)
        }

        override fun getConstructor(value: MsgAddressExt): TlbConstructor<out MsgAddressExt> = when (value) {
            is MsgAddressExtNone -> addrNoneConstructor
            is MsgAddressExtOrdinary -> addrExternConstructor
        }
    }
}

@SerialName("addr_extern")
@Serializable
data class MsgAddressExtOrdinary(
    val len: Int,
    @SerialName("external_address")
    val externalAddress: BitString
) : MsgAddressExt {
    init {
        require(externalAddress.size == len) { "externalAddress.size expected: $len actual: ${externalAddress.size}" }
    }

    constructor(externalAddress: ByteArray) : this(externalAddress.toBitString())
    constructor(externalAddress: BitString) : this(externalAddress.size, externalAddress)

    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<MsgAddressExtOrdinary> = AddrExternTlbConstructor
    }

    private object AddrExternTlbConstructor : TlbConstructor<MsgAddressExtOrdinary>(
        schema = "addr_extern\$01 len:(## 9) external_address:(bits len) = MsgAddressExt;"
    ) {
        override fun storeTlb(
            cellBuilder: CellBuilder, value: MsgAddressExtOrdinary
        ) = cellBuilder {
            storeUInt(value.len, 9)
            storeBits(value.externalAddress)
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): MsgAddressExtOrdinary = cellSlice {
            val len = loadUInt(9).toInt()
            val externalAddress = loadBitString(len)
            MsgAddressExtOrdinary(len, externalAddress)
        }
    }
}

@SerialName("addr_none")
@Serializable
object MsgAddressExtNone : MsgAddressExt {
    override fun toString(): String = "addr_none"

    @JvmStatic
    fun tlbCodec(): TlbConstructor<MsgAddressExtNone> = AddrNoneTlbConstructor

    private object AddrNoneTlbConstructor : TlbConstructor<MsgAddressExtNone>(
        schema = "addr_none\$00 = MsgAddressExt;"
    ) {
        override fun storeTlb(
            cellBuilder: CellBuilder,
            value: MsgAddressExtNone
        ) {
        }

        override fun loadTlb(cellSlice: CellSlice): MsgAddressExtNone {
            return MsgAddressExtNone
        }
    }
}
