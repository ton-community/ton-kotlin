package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb
import kotlin.jvm.JvmStatic

@SerialName("ext_out_msg_info")
@Serializable
data class ExtOutMsgInfo(
    val src: MsgAddressInt,
    val dest: MsgAddressExt,
    val created_lt: Long,
    val created_at: Int
) : CommonMsgInfo {

    override fun toString(): String = buildString {
        append("(ext_out_msg_info\n")
        append("src:")
        append(src)
        append(" dest:")
        append(dest)
        append(" created_lt:")
        append(created_lt)
        append(" created_at:")
        append(created_at)
        append(")")
    }

    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<ExtOutMsgInfo> = ExtOutMsgInfoTlbConstructor
    }
}

private object ExtOutMsgInfoTlbConstructor : TlbConstructor<ExtOutMsgInfo>(
    schema = "ext_out_msg_info\$11 src:MsgAddressInt dest:MsgAddressExt created_lt:uint64 created_at:uint32 = CommonMsgInfo;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder, value: ExtOutMsgInfo
    ) = cellBuilder {
        storeTlb(MsgAddressInt, value.src)
        storeTlb(MsgAddressExt, value.dest)
        storeUInt(value.created_lt, 64)
        storeUInt(value.created_at, 32)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): ExtOutMsgInfo = cellSlice {
        val src = loadTlb(MsgAddressInt)
        val dest = loadTlb(MsgAddressExt)
        val createdLt = loadUInt(64).toLong()
        val createdAt = loadUInt(32).toInt()
        ExtOutMsgInfo(src, dest, createdLt, createdAt)
    }
}
