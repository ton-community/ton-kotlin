package org.ton.block.message

import kotlinx.serialization.SerialName
import org.ton.block.currency.Coins
import org.ton.block.currency.CurrencyCollection
import org.ton.block.message.address.AddrInt
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbConstructor
import org.ton.tlb.TlbPrettyPrinter
import org.ton.tlb.loadTlb
import org.ton.tlb.providers.TlbConstructorProvider
import org.ton.tlb.storeTlb

public data class IntMsgInfo(
    @SerialName("ihr_disabled") val ihrDisabled: Boolean,
    val bounce: Boolean,
    val bounced: Boolean,
    val src: AddrInt,
    val dest: AddrInt,
    val value: CurrencyCollection,
    @SerialName("ihr_fee") val ihr_fee: Coins,
    @SerialName("fwd_fee") val fwd_fee: Coins,
    @SerialName("created_lt") val created_lt: Long,
    @SerialName("created_at") val created_at: Int
) : CommonMsgInfo {
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
        type("int_msg_info") {
            field("ihr_disabled", ihrDisabled)
            field("bounce", bounce)
            field("bounced", bounced)
            field("src", src)
            field("dest", dest)
            field("value", value)
            field("ihr_fee", ihr_fee)
            field("fwd_fee", fwd_fee)
            field("created_lt", created_lt)
            field("created_at", created_at)
        }
    }

    override fun toString(): String = print().toString()

    public companion object : TlbConstructorProvider<IntMsgInfo> by IntMsgInfoTlbConstructor
}

private object IntMsgInfoTlbConstructor : TlbConstructor<IntMsgInfo>(
    schema = "int_msg_info\$0 ihr_disabled:Bool bounce:Bool bounced:Bool src:MsgAddressInt dest:MsgAddressInt value:CurrencyCollection ihr_fee:Coins fwd_fee:Coins created_lt:uint64 created_at:uint32 = CommonMsgInfo;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder, value: IntMsgInfo
    ) = cellBuilder {
        storeBit(value.ihrDisabled)
        storeBit(value.bounce)
        storeBit(value.bounced)
        storeTlb(AddrInt, value.src)
        storeTlb(AddrInt, value.dest)
        storeTlb(CurrencyCollection.Tlb, value.value)
        storeTlb(Coins.Tlb, value.ihr_fee)
        storeTlb(Coins.Tlb, value.fwd_fee)
        storeUInt(value.created_lt, 64)
        storeUInt(value.created_at, 32)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): IntMsgInfo = cellSlice {
        val ihrDisabled = loadBit()
        val bounce = loadBit()
        val bounced = loadBit()
        val src = loadTlb(AddrInt)
        val dest = loadTlb(AddrInt)
        val value = loadTlb(CurrencyCollection.Tlb)
        val ihrFee = loadTlb(Coins.Tlb)
        val fwdFee = loadTlb(Coins.Tlb)
        val createdLt = loadULong(64).toLong()
        val createdAt = loadUInt().toInt()
        IntMsgInfo(
            ihrDisabled, bounce, bounced, src, dest, value, ihrFee, fwdFee, createdLt, createdAt
        )
    }
}
