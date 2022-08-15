package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.*
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.providers.TlbConstructorProvider
import org.ton.tlb.storeTlb

@Serializable
@SerialName("msg_import_ihr")
data class MsgImportIhr(
    val msg: Message<Cell>,
    val transaction: Transaction,
    val ihr_fee: Coins,
    val proof_created: Cell
) : InMsg {
    companion object : TlbConstructorProvider<MsgImportIhr> by MsgImportIhrTlbConstructor
}

private object MsgImportIhrTlbConstructor : TlbConstructor<MsgImportIhr>(
    schema = "msg_import_ext\$000 msg:^(Message Any) transaction:^Transaction = InMsg;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: MsgImportIhr
    ) = cellBuilder {
        storeRef {
            storeTlb(Message.Any, value.msg)
        }
        storeRef {
            storeTlb(Transaction, value.transaction)
        }
        storeTlb(Coins, value.ihr_fee)
        storeRef(value.proof_created)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): MsgImportIhr = cellSlice {
        val msg = loadRef {
            loadTlb(Message.Any)
        }
        val transaction = loadRef {
            loadTlb(Transaction)
        }
        val ihrFee = loadTlb(Coins)
        val proofCreated = loadRef()
        MsgImportIhr(msg, transaction, ihrFee, proofCreated)
    }
}
