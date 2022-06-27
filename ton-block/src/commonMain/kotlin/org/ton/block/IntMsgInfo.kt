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
    @SerialName("ihr_disabled")
    val ihrDisabled: Boolean,
    val bounce: Boolean,
    val bounced: Boolean,
    val src: MsgAddressInt,
    val dest: MsgAddressInt,
    val value: CurrencyCollection,
    @SerialName("ihr_fee")
    val ihrFee: Coins = Coins(),
    @SerialName("fwd_fee")
    val fwdFee: Coins = Coins(),
    @SerialName("created_lt")
    val createdLt: Long = 0,
    @SerialName("created_at")
    val createdAt: Int = 0
) : CommonMsgInfo {
    companion object {
        fun tlbCodec(): TlbConstructor<IntMsgInfo> = IntMsgInfoTlbConstructor()
    }
}

private class IntMsgInfoTlbConstructor : TlbConstructor<IntMsgInfo>(
    schema = "int_msg_info\$0 ihr_disabled:Bool bounce:Bool bounced:Bool " + "src:MsgAddressInt dest:MsgAddressInt " + "value:CurrencyCollection ihr_fee:Coins fwd_fee:Coins " + "created_lt:uint64 created_at:uint32 = CommonMsgInfo;"
) {
    private val msgAddressIntCodec by lazy {
        MsgAddressInt.tlbCodec()
    }
    private val currencyCollectionCodec by lazy {
        CurrencyCollection.tlbCodec()
    }
    private val coinsCodec by lazy {
        Coins.tlbCodec()
    }

    override fun storeTlb(
        cellBuilder: CellBuilder, value: IntMsgInfo
    ) = cellBuilder {
        storeBit(value.ihrDisabled)
        storeBit(value.bounce)
        storeBit(value.bounced)
        storeTlb(msgAddressIntCodec, value.src)
        storeTlb(msgAddressIntCodec, value.dest)
        storeTlb(currencyCollectionCodec, value.value)
        storeTlb(coinsCodec, value.ihrFee)
        storeTlb(coinsCodec, value.fwdFee)
        storeUInt(value.createdLt, 64)
        storeUInt(value.createdAt, 32)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): IntMsgInfo = cellSlice {
        val ihrDisabled = loadBit()
        val bounce = loadBit()
        val bounced = loadBit()
        val src = loadTlb(msgAddressIntCodec)
        val dest = loadTlb(msgAddressIntCodec)
        val value = loadTlb(currencyCollectionCodec)
        val ihrFee = loadTlb(coinsCodec)
        val fwdFee = loadTlb(coinsCodec)
        val createdLt = loadUInt(64).toLong()
        val createdAt = loadUInt(32).toInt()
        IntMsgInfo(
            ihrDisabled, bounce, bounced, src, dest, value, ihrFee, fwdFee, createdLt, createdAt
        )
    }
}