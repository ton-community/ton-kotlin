package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@Serializable
@SerialName("msg_import_fin")
data class MsgImportFin(
    val in_msg: MsgEnvelope,
    val transaction: Transaction,
    val fwd_fee: Coins
) : InMsg {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<MsgImportFin> = MsgImportFinTlbConstructor
    }
}

private object MsgImportFinTlbConstructor : TlbConstructor<MsgImportFin>(
    schema = "msg_import_fin\$100 in_msg:^MsgEnvelope transaction:^Transaction fwd_fee:Coins = InMsg;"
) {
    val msgEnvelope by lazy { MsgEnvelope.tlbCodec() }
    val transaction by lazy { Transaction.tlbCodec() }
    val coins by lazy { Coins.tlbCodec() }

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: MsgImportFin
    ) = cellBuilder {
        storeRef {
            storeTlb(msgEnvelope, value.in_msg)
        }
        storeRef {
            storeTlb(transaction, value.transaction)
        }
        storeTlb(coins, value.fwd_fee)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): MsgImportFin = cellSlice {
        val inMsg = loadRef {
            loadTlb(msgEnvelope)
        }
        val transaction = loadRef {
            loadTlb(transaction)
        }
        val fwdFee = loadTlb(coins)
        MsgImportFin(inMsg, transaction, fwdFee)
    }
}

