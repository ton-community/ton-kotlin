@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor

@Suppress("NOTHING_TO_INLINE")
inline fun MsgAddressExt(externalAddress: BitString? = null): MsgAddressExt = MsgAddressExt.of(externalAddress)

@JsonClassDiscriminator("@type")
@Serializable
sealed interface MsgAddressExt : MsgAddress {
    @SerialName("addr_none")
    @Serializable
    object AddrNone : MsgAddressExt {
        override fun toString(): String = "addr_none"
    }

    @SerialName("addr_extern")
    @Serializable
    data class AddrExtern(
        val len: Int,
        @SerialName("external_address")
        val externalAddress: BitString
    ) : MsgAddressExt {
        init {
            require(len == externalAddress.size)
        }

        constructor(externalAddress: BitString) : this(externalAddress.size, externalAddress)
    }

    companion object {
        @JvmStatic
        fun of(externalAddress: BitString? = null): MsgAddressExt {
            return if (externalAddress.isNullOrEmpty()) {
                AddrNone
            } else {
                AddrExtern(externalAddress)
            }
        }

        @JvmStatic
        fun tlbCodec(): TlbCodec<MsgAddressExt> = MsgAddressExtTlbCombinator()

        internal class MsgAddressExtTlbCombinator : TlbCombinator<MsgAddressExt>() {
            private val addrNoneConstructor by lazy { AddrNoneTlbConstructor() }
            private val addrExternConstructor by lazy { AddrExternTlbConstructor() }

            override val constructors: List<TlbConstructor<out MsgAddressExt>> by lazy {
                listOf(addrNoneConstructor, addrExternConstructor)
            }

            override fun getConstructor(value: MsgAddressExt): TlbConstructor<out MsgAddressExt> = when (value) {
                is AddrNone -> addrNoneConstructor
                is AddrExtern -> addrExternConstructor
            }

            private class AddrNoneTlbConstructor : TlbConstructor<AddrNone>(
                schema = "addr_none\$00 = MsgAddressExt;"
            ) {
                override fun storeTlb(
                    cellBuilder: CellBuilder,
                    value: AddrNone
                ) {
                }

                override fun loadTlb(cellSlice: CellSlice): AddrNone {
                    return AddrNone
                }
            }

            private class AddrExternTlbConstructor : TlbConstructor<AddrExtern>(
                schema = "addr_extern\$01 len:(## 9) external_address:(bits len) = MsgAddressExt;"
            ) {
                override fun storeTlb(
                    cellBuilder: CellBuilder,
                    value: AddrExtern
                ) = cellBuilder {
                    storeUInt(value.len, 9)
                    storeBits(value.externalAddress)
                }

                override fun loadTlb(
                    cellSlice: CellSlice
                ): AddrExtern = cellSlice {
                    val len = loadUInt(9).toInt()
                    val externalAddress = loadBitString(len)
                    AddrExtern(len, externalAddress)
                }
            }
        }
    }
}
