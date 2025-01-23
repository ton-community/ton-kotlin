package org.ton.block.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
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

@SerialName("ext_out_msg_info")
@Serializable
public data class ExtOutMsgInfo(
    val src: AddrInt,
    val dest: AddrExt?,
    @SerialName("created_lt") val createdLt: ULong,
    @SerialName("created_at") val createdAt: UInt
) : CommonMsgInfo {
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
        type("ext_out_msg_info") {
            field("src", src)
            field("dest", dest)
            field("created_lt", createdLt)
            field("created_at", createdAt)
        }
    }

    override fun toString(): String = print().toString()

    public companion object : TlbConstructorProvider<ExtOutMsgInfo> by ExtOutMsgInfoTlbConstructor
}

private object ExtOutMsgInfoTlbConstructor : TlbConstructor<ExtOutMsgInfo>(
    schema = "ext_out_msg_info\$11 src:MsgAddressInt dest:MsgAddressExt created_lt:uint64 created_at:uint32 = CommonMsgInfo;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder, value: ExtOutMsgInfo
    ) = cellBuilder {
        storeTlb(AddrInt, value.src)
        if (value.dest != null) {
            storeTlb(AddrExt, value.dest)
        } else {
            storeUInt(0, 2)
        }
        storeUInt(value.createdLt.toLong(), 64)
        storeUInt(value.createdAt.toInt(), 32)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): ExtOutMsgInfo = cellSlice {
        val src = loadTlb(AddrInt)
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
        ExtOutMsgInfo(src, dest, createdLt, createdAt)
    }
}
