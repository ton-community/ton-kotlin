@file:Suppress("NOTHING_TO_INLINE")

package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.constructor.tlbCodec
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@Serializable
@SerialName("msg_import_ext")
data class MsgImportExt(
     val msg: Message<Cell>,
     val transaction: Transaction
) : InMsg {
     companion object {
          @JvmStatic
          fun tlbCodec(): TlbConstructor<MsgImportExt> = MsgImportExtTlbConstructor
     }
}

private object MsgImportExtTlbConstructor : TlbConstructor<MsgImportExt>(
     schema = "msg_import_ext\$000 msg:^(Message Any) transaction:^Transaction = InMsg;"
) {
     val messageAny by lazy { Message.tlbCodec(Cell.tlbCodec()) }
     val transaction by lazy { Transaction.tlbCodec() }

     override fun storeTlb(
          cellBuilder: CellBuilder,
          value: MsgImportExt
     ) = cellBuilder {
          storeRef {
               storeTlb(messageAny, value.msg)
          }
          storeRef {
               storeTlb(transaction, value.transaction)
          }
     }

     override fun loadTlb(
          cellSlice: CellSlice
     ): MsgImportExt = cellSlice {
          val msg = loadRef {
               loadTlb(messageAny)
          }
          val transaction = loadRef {
               loadTlb(transaction)
          }
          MsgImportExt(msg, transaction)
     }
}