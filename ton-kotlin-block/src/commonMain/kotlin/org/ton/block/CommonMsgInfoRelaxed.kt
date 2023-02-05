@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.providers.TlbCombinatorProvider
import org.ton.tlb.storeTlb

@JsonClassDiscriminator("@type")
@Serializable
public sealed interface CommonMsgInfoRelaxed {
    @SerialName("int_msg_info")
    public data class IntMsgInfoRelaxed(
        val ihrDisabled: Boolean = true,
        val bounce: Boolean,
        val bounced: Boolean = false,
        val src: MsgAddress = MsgAddressExt(),
        val dest: MsgAddressInt,
        val value: CurrencyCollection,
        val ihrFee: Coins = Coins(),
        val fwdFee: Coins = Coins(),
        val createdLt: ULong = 0u,
        val createdAt: UInt = 0u
    ) : CommonMsgInfoRelaxed {
        public constructor(dest: MsgAddressInt, bounce: Boolean, coins: Coins) : this(
            dest = dest,
            bounce = bounce,
            value = CurrencyCollection(coins, ExtraCurrencyCollection())
        )

        public constructor(dest: MsgAddressInt, bounce: Boolean, value: CurrencyCollection) : this(
            ihrDisabled = true,
            bounce = bounce,
            bounced = false,
            src = MsgAddressExt(),
            dest = dest,
            value = value
        )
    }

    @SerialName("ext_out_msg_info")
    data class ExtOutMsgInfoRelaxed(
        val src: MsgAddress,
        val dest: MsgAddressExt,
        val created_lt: ULong,
        val created_at: UInt
    ) : CommonMsgInfoRelaxed

    companion object : TlbCombinatorProvider<CommonMsgInfoRelaxed> by CommonMsgInfoRelaxedTlbCombinator
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
            storeTlb(MsgAddress, value.src)
            storeTlb(MsgAddressInt, value.dest)
            storeTlb(CurrencyCollection, value.value)
            storeTlb(Coins, value.ihrFee)
            storeTlb(Coins, value.fwdFee)
            storeUInt64(value.createdLt)
            storeUInt32(value.createdAt)
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): CommonMsgInfoRelaxed.IntMsgInfoRelaxed = cellSlice {
            val ihrDisabled = loadBit()
            val bounce = loadBit()
            val bounced = loadBit()
            val src = loadTlb(MsgAddress)
            val dest = loadTlb(MsgAddressInt)
            val value = loadTlb(CurrencyCollection)
            val ihrFee = loadTlb(Coins)
            val fwdFee = loadTlb(Coins)
            val createdLt = loadUInt64()
            val createdAt = loadUInt32()
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
            storeTlb(MsgAddress, value.src)
            storeTlb(MsgAddressExt, value.dest)
            storeUInt64(value.created_lt)
            storeUInt32(value.created_at)
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): CommonMsgInfoRelaxed.ExtOutMsgInfoRelaxed = cellSlice {
            val src = loadTlb(MsgAddress)
            val dest = loadTlb(MsgAddressExt)
            val createdLt = loadUInt64()
            val createdAt = loadUInt32()
            CommonMsgInfoRelaxed.ExtOutMsgInfoRelaxed(src, dest, createdLt, createdAt)
        }
    }
}
