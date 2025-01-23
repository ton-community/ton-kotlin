package org.ton.block.message.export

import org.ton.block.message.envelope.MsgEnvelope
import org.ton.block.message.inmsg.InMsg
import org.ton.block.transaction.Transaction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.*
import org.ton.tlb.providers.TlbConstructorProvider

public data class OutMsgImmediate(
    val outMsg: CellRef<MsgEnvelope>,
    val transaction: CellRef<Transaction>,
    val reimport: CellRef<InMsg>
) : OutMsg, TlbObject {
    public companion object : TlbConstructorProvider<OutMsgImmediate> by MsgExportImmTlbConstructor

    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
        type("msg_export_imm") {
            field("out_msg", outMsg)
            field("transaction", transaction)
            field("reimport", reimport)
        }
    }

    override fun toString(): String = print().toString()
}

private object MsgExportImmTlbConstructor : TlbConstructor<OutMsgImmediate>(
    schema = "msg_export_imm\$010 out_msg:^MsgEnvelope transaction:^Transaction reimport:^InMsg = OutMsg;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: OutMsgImmediate
    ) = cellBuilder {
        storeRef(MsgEnvelope, value.outMsg)
        storeRef(Transaction, value.transaction)
        storeRef(InMsg.Companion, value.reimport)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): OutMsgImmediate = cellSlice {
        val outMsg = loadRef(MsgEnvelope)
        val transaction = loadRef(Transaction)
        val reimport = loadRef(InMsg.Companion)
        OutMsgImmediate(outMsg, transaction, reimport)
    }
}
