package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@SerialName("ext_in_msg_info")
@Serializable
data class ExtInMsgInfo(
    val src: MsgAddressExt,
    val dest: MsgAddressInt,
    @SerialName("import_fee")
    val importFee: Coins = Coins()
) : CommonMsgInfo {
    constructor(
        dest: MsgAddressInt,
        importFee: Coins = Coins()
    ) : this(AddrNone, dest, importFee)

    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<ExtInMsgInfo> = ExtInMsgInfoTlbConstructor()
    }
}

private class ExtInMsgInfoTlbConstructor : TlbConstructor<ExtInMsgInfo>(
    schema = "ext_in_msg_info\$10 src:MsgAddressExt dest:MsgAddressInt import_fee:Coins = CommonMsgInfo;"
) {
    private val msgAddressExtCodec by lazy {
        MsgAddressExt.tlbCodec()
    }
    private val msgAddressIntCodec by lazy {
        MsgAddressInt.tlbCodec()
    }
    private val coinsCodec by lazy {
        Coins.tlbCodec()
    }

    override fun storeTlb(
        cellBuilder: CellBuilder, value: ExtInMsgInfo
    ) = cellBuilder {
        storeTlb(msgAddressExtCodec, value.src)
        storeTlb(msgAddressIntCodec, value.dest)
        storeTlb(coinsCodec, value.importFee)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): ExtInMsgInfo = cellSlice {
        val src = loadTlb(msgAddressExtCodec)
        val dest = loadTlb(msgAddressIntCodec)
        val importFee = loadTlb(coinsCodec)
        ExtInMsgInfo(src, dest, importFee)
    }
}

