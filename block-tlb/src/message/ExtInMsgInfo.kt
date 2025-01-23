package org.ton.block.message

import kotlinx.serialization.SerialName
import org.ton.block.currency.Coins
import org.ton.block.message.address.AddrExt
import org.ton.block.message.address.AddrInt
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbConstructor
import org.ton.tlb.TlbPrettyPrinter
import org.ton.tlb.loadTlb
import org.ton.tlb.providers.TlbConstructorProvider
import org.ton.tlb.storeTlb

public data class ExtInMsgInfo(
    val src: AddrExt?,
    val dest: AddrInt,
    @SerialName("import_fee") val importFee: Coins
) : CommonMsgInfo {
    public constructor(
        dest: AddrInt,
        importFee: Coins = Coins.ZERO
    ) : this(null, dest, importFee)

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
        if (value.src != null) {
            storeTlb(AddrExt, value.src)
        } else {
            storeUInt(0, 2)
        }
        storeTlb(AddrInt, value.dest)
        storeTlb(Coins.Tlb, value.importFee)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): ExtInMsgInfo = cellSlice {
        val src = when (val tag = preloadUInt(2)) {
            0b01u -> loadTlb(AddrExt)
            0b00u -> {
                skipBits(2)
                null
            }

            else -> throw IllegalStateException("Invalid tag: $tag")
        }
        val dest = loadTlb(AddrInt)
        val importFee = loadTlb(Coins.Tlb)
        ExtInMsgInfo(src, dest, importFee)
    }
}
