@file:Suppress("NOTHING_TO_INLINE")

package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.*
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.providers.TlbConstructorProvider
import org.ton.tlb.storeTlb

@Serializable
@SerialName("msg_import_ext")
data class MsgImportExt(
    val msg: Message<Cell>,
    val transaction: Transaction
) : InMsg {
    companion object : TlbConstructorProvider<MsgImportExt> by MsgImportExtTlbConstructor
}

private object MsgImportExtTlbConstructor : TlbConstructor<MsgImportExt>(
    schema = "msg_import_ext\$000 msg:^(Message Any) transaction:^Transaction = InMsg;"
) {

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: MsgImportExt
    ) = cellBuilder {
        storeRef {
            storeTlb(Message.Any, value.msg)
        }
        storeRef {
            storeTlb(Transaction, value.transaction)
        }
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): MsgImportExt = cellSlice {
        val msg = loadRef {
            loadTlb(Message.Any)
        }
        val transaction = loadRef {
            loadTlb(Transaction)
        }
        MsgImportExt(msg, transaction)
    }
}
