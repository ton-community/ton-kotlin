package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb
import kotlin.jvm.JvmStatic

@SerialName("ext_in_msg_info")
@Serializable
data class ExtInMsgInfo(
    val src: MsgAddressExt,
    val dest: MsgAddressInt,
    val import_fee: Coins = Coins()
) : CommonMsgInfo {
    constructor(
        dest: MsgAddressInt,
        importFee: Coins = Coins()
    ) : this(AddrNone, dest, importFee)

    companion object : TlbCodec<ExtInMsgInfo> by ExtInMsgInfoTlbConstructor {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<ExtInMsgInfo> = ExtInMsgInfoTlbConstructor
    }

    override fun toString(): String = buildString {
        append("(ext_in_msg_info\n")
        append("src:$src ")
        append("dest:$dest ")
        append("import_fee:$import_fee")
        append(")")
    }
}

private object ExtInMsgInfoTlbConstructor : TlbConstructor<ExtInMsgInfo>(
    schema = "ext_in_msg_info\$10 src:MsgAddressExt dest:MsgAddressInt import_fee:Coins = CommonMsgInfo;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder, value: ExtInMsgInfo
    ) = cellBuilder {
        storeTlb(MsgAddressExt, value.src)
        storeTlb(MsgAddressInt, value.dest)
        storeTlb(Coins, value.import_fee)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): ExtInMsgInfo = cellSlice {
        val src = loadTlb(MsgAddressExt)
        val dest = loadTlb(MsgAddressInt)
        val importFee = loadTlb(Coins)
        ExtInMsgInfo(src, dest, importFee)
    }
}
