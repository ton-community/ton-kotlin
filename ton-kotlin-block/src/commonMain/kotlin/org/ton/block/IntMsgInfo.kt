package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@SerialName("int_msg_info")
@Serializable
data class IntMsgInfo(
    val ihr_disabled: Boolean,
    val bounce: Boolean,
    val bounced: Boolean,
    val src: MsgAddressInt,
    val dest: MsgAddressInt,
    val value: CurrencyCollection,
    val ihr_fee: Coins = Coins(),
    val fwd_fee: Coins = Coins(),
    val created_lt: Long = 0,
    val created_at: Int = 0
) : CommonMsgInfo {
    companion object {
        fun tlbCodec(): TlbConstructor<IntMsgInfo> = IntMsgInfoTlbConstructor()
    }

    override fun toString(): String = buildString {
        append("(int_msg_info\n")
        append("ihr_disabled:$ihr_disabled ")
        append("bounce:$bounce ")
        append("bounced:$bounced ")
        append("src:$src ")
        append("dest:$dest ")
        append("value:$value ")
        append("ihr_fee:$ihr_fee ")
        append("fwd_fee:$fwd_fee ")
        append("created_lt:$created_lt ")
        append("created_at:$created_at")
        append(")")
    }
}

private class IntMsgInfoTlbConstructor : TlbConstructor<IntMsgInfo>(
    schema = "int_msg_info\$0 ihr_disabled:Bool bounce:Bool bounced:Bool src:MsgAddressInt dest:MsgAddressInt value:CurrencyCollection ihr_fee:Coins fwd_fee:Coins created_lt:uint64 created_at:uint32 = CommonMsgInfo;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder, value: IntMsgInfo
    ) = cellBuilder {
        storeBit(value.ihr_disabled)
        storeBit(value.bounce)
        storeBit(value.bounced)
        storeTlb(MsgAddressInt, value.src)
        storeTlb(MsgAddressInt, value.dest)
        storeTlb(CurrencyCollection, value.value)
        storeTlb(Coins, value.ihr_fee)
        storeTlb(Coins, value.fwd_fee)
        storeUInt(value.created_lt, 64)
        storeUInt(value.created_at, 32)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): IntMsgInfo = cellSlice {
        val ihrDisabled = loadBit()
        val bounce = loadBit()
        val bounced = loadBit()
        val src = loadTlb(MsgAddressInt)
        val dest = loadTlb(MsgAddressInt)
        val value = loadTlb(CurrencyCollection)
        val ihrFee = loadTlb(Coins)
        val fwdFee = loadTlb(Coins)
        val createdLt = loadUInt(64).toLong()
        val createdAt = loadUInt(32).toInt()
        IntMsgInfo(
            ihrDisabled, bounce, bounced, src, dest, value, ihrFee, fwdFee, createdLt, createdAt
        )
    }
}