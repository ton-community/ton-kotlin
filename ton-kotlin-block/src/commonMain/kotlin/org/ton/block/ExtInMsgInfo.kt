package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.*
import org.ton.tlb.providers.TlbConstructorProvider

@Serializable
@SerialName("ext_in_msg_info")
public data class ExtInMsgInfo(
    val src: MsgAddressExt,
    val dest: MsgAddressInt,
    @SerialName("import_fee") val importFee: Coins
) : CommonMsgInfo {
    public constructor(
        dest: MsgAddressInt,
        importFee: Coins = Coins()
    ) : this(AddrNone, dest, importFee)

    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
        type("ext_in_msg_info") {
            field("src", src)
            field("dest", dest)
            field("import_fee", importFee)
        }
    }

    override fun toString(): String = print().toString()

    public companion object : TlbConstructorProvider<ExtInMsgInfo> by ExtInMsgInfoTlbConstructor
}

private object ExtInMsgInfoTlbConstructor : TlbConstructor<ExtInMsgInfo>(
    schema = "ext_in_msg_info\$10 src:MsgAddressExt dest:MsgAddressInt import_fee:Coins = CommonMsgInfo;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder, value: ExtInMsgInfo
    ) = cellBuilder {
        storeTlb(MsgAddressExt, value.src)
        storeTlb(MsgAddressInt, value.dest)
        storeTlb(Coins, value.importFee)
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
