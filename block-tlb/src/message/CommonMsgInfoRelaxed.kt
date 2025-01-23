@file:Suppress("OPT_IN_USAGE")

package org.ton.block.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.block.currency.Coins
import org.ton.block.currency.CurrencyCollection
import org.ton.block.currency.ExtraCurrencyCollection
import org.ton.block.message.address.AddrExt
import org.ton.block.message.address.AddrInt
import org.ton.block.message.address.AddrStd
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.*
import org.ton.tlb.providers.TlbCombinatorProvider


@Serializable
public sealed interface CommonMsgInfoRelaxed : TlbObject {
    @SerialName("int_msg_info")
    public data class IntMsgInfoRelaxed(
        val ihrDisabled: Boolean = true,
        val bounce: Boolean = true,
        val bounced: Boolean = false,
        val src: AddrInt? = null,
        val dest: AddrInt = AddrStd(),
        val value: CurrencyCollection = CurrencyCollection.Companion.ZERO,
        val ihrFee: Coins = Coins.ZERO,
        val fwdFee: Coins = Coins.ZERO,
        val createdLt: ULong = 0u,
        val createdAt: UInt = 0u
    ) : CommonMsgInfoRelaxed {
        public constructor(dest: AddrInt, bounce: Boolean, value: Coins) : this(
            dest = dest,
            bounce = bounce,
            value = CurrencyCollection(value, ExtraCurrencyCollection())
        )

        public constructor(dest: AddrInt, bounce: Boolean, value: CurrencyCollection) : this(
            ihrDisabled = true,
            bounce = bounce,
            bounced = false,
            src = null,
            dest = dest,
            value = value
        )

        override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
            type("int_msg_info") {
                field("ihr_disabled", ihrDisabled)
                field("bounce", bounce)
                field("bounced", bounced)
                field("src", src)
                field("dest", dest)
                field("value", value)
                field("ihr_fee", ihrFee)
                field("fwd_fee", fwdFee)
                field("created_lt", createdLt)
                field("created_at", createdAt)
            }
        }

        override fun toString(): String = print().toString()
    }

    @SerialName("ext_out_msg_info")
    public data class ExtOutMsgInfoRelaxed(
        val src: AddrInt?,
        val dest: AddrExt?,
        val createdLt: ULong,
        val createdAt: UInt
    ) : CommonMsgInfoRelaxed {
        override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
            type("ext_out_msg_info") {
                field("src", src)
                field("dest", dest)
                field("created_lt", createdLt)
                field("created_at", createdAt)
            }
        }

        override fun toString(): String = print().toString()
    }

    public companion object : TlbCombinatorProvider<CommonMsgInfoRelaxed> by CommonMsgInfoRelaxedTlbCombinator
}

private object CommonMsgInfoRelaxedTlbCombinator : TlbCombinator<CommonMsgInfoRelaxed>(
    CommonMsgInfoRelaxed::class,
    CommonMsgInfoRelaxed.IntMsgInfoRelaxed::class to IntMsgInfoTlbConstructor,
    CommonMsgInfoRelaxed.ExtOutMsgInfoRelaxed::class to ExtOutMsgInfoTlbConstructor
) {
    private object IntMsgInfoTlbConstructor : TlbConstructor<CommonMsgInfoRelaxed.IntMsgInfoRelaxed>(
        schema = "int_msg_info\$0 ihr_disabled:Bool bounce:Bool bounced:Bool" +
                " src:MsgAddress dest:MsgAddressInt" +
                " value:CurrencyCollection ihr_fee:Coins fwd_fee:Coins" +
                " created_lt:uint64 created_at:uint32 = CommonMsgInfoRelaxed;"
    ) {
        override fun storeTlb(
            cellBuilder: CellBuilder, value: CommonMsgInfoRelaxed.IntMsgInfoRelaxed
        ) = cellBuilder {
            storeBit(value.ihrDisabled)
            storeBit(value.bounce)
            storeBit(value.bounced)
            if (value.src != null) {
                storeTlb(AddrInt, value.src)
            } else {
                storeUInt(0, 2)
            }
            storeTlb(AddrInt, value.dest)
            storeTlb(CurrencyCollection.Tlb, value.value)
            storeTlb(Coins.Tlb, value.ihrFee)
            storeTlb(Coins.Tlb, value.fwdFee)
            storeUInt64(value.createdLt)
            storeUInt32(value.createdAt)
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): CommonMsgInfoRelaxed.IntMsgInfoRelaxed = cellSlice {
            val ihrDisabled = loadBit()
            val bounce = loadBit()
            val bounced = loadBit()
            val src = if (preloadBit()) {
                loadTlb(AddrInt.Companion)
            } else {
                skipBits(2)
                null
            }
            val dest = loadTlb(AddrInt)
            val value = loadTlb(CurrencyCollection.Tlb)
            val ihrFee = loadTlb(Coins.Tlb)
            val fwdFee = loadTlb(Coins.Tlb)
            val createdLt = loadULong()
            val createdAt = loadUInt()
            CommonMsgInfoRelaxed.IntMsgInfoRelaxed(
                ihrDisabled, bounce, bounced, src, dest, value, ihrFee, fwdFee, createdLt, createdAt
            )
        }
    }

    private object ExtOutMsgInfoTlbConstructor : TlbConstructor<CommonMsgInfoRelaxed.ExtOutMsgInfoRelaxed>(
        schema = "ext_out_msg_info\$11 src:MsgAddress dest:MsgAddressExt" +
                " created_lt:uint64 created_at:uint32 = CommonMsgInfoRelaxed;"
    ) {

        override fun storeTlb(
            cellBuilder: CellBuilder, value: CommonMsgInfoRelaxed.ExtOutMsgInfoRelaxed
        ) = cellBuilder {
            if (value.src != null) {
                storeTlb(AddrInt, value.src)
            } else {
                storeUInt(0, 2)
            }
            if (value.dest != null) {
                storeTlb(AddrExt, value.dest)
            } else {
                storeUInt(0, 2)
            }
            storeUInt64(value.createdLt)
            storeUInt32(value.createdAt)
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): CommonMsgInfoRelaxed.ExtOutMsgInfoRelaxed = cellSlice {
            val src = if (preloadBit()) {
                loadTlb(AddrInt)
            } else {
                skipBits(2)
                null
            }
            val dest = when (val tag = preloadUInt(2)) {
                0b01u -> loadTlb(AddrExt)
                0b00u -> {
                    skipBits(2)
                    null
                }

                else -> throw IllegalStateException("Invalid tag: $tag")
            }
            val createdLt = loadULong()
            val createdAt = loadUInt()
            CommonMsgInfoRelaxed.ExtOutMsgInfoRelaxed(src, dest, createdLt, createdAt)
        }
    }
}
