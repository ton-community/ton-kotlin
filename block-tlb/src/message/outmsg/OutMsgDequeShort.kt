package org.ton.block.message.export

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbConstructor
import org.ton.tlb.TlbPrettyPrinter
import org.ton.tlb.providers.TlbConstructorProvider

@Serializable
@SerialName("msg_export_deq_short")
public data class OutMsgDequeShort(
    val msgEnvHash: BitString,
    val nextWorkchain: Int,
    val nextAddrPfx: ULong,
    val importBlockLt: ULong
) : OutMsg {
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
        type("msg_export_deq_short") {
            field("msg_env_hash", msgEnvHash)
            field("next_workchain", nextWorkchain)
            field("next_addr_pfx", nextAddrPfx)
            field("import_block_lt", importBlockLt)
        }
    }

    override fun toString(): String = print().toString()

    public companion object : TlbConstructorProvider<OutMsgDequeShort> by MsgExportDeqShortTlbConstructor
}

private object MsgExportDeqShortTlbConstructor : TlbConstructor<OutMsgDequeShort>(
    schema = "msg_export_deq_short\$1101 msg_env_hash:bits256 " +
            "next_workchain:int32 next_addr_pfx:uint64 " +
            "import_block_lt:uint64 = OutMsg;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: OutMsgDequeShort
    ) = cellBuilder {
        storeBits(value.msgEnvHash)
        storeInt(value.nextWorkchain, 32)
        storeUInt64(value.nextAddrPfx)
        storeUInt64(value.importBlockLt)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): OutMsgDequeShort = cellSlice {
        val msgEnvHash = loadBits(256)
        val nextWorkchain = loadInt(32)
        val nextAddrPfx = loadULong()
        val importBlockLt = loadULong()
        OutMsgDequeShort(msgEnvHash, nextWorkchain, nextAddrPfx, importBlockLt)
    }
}
