package org.ton.kotlin.account

import org.ton.kotlin.cell.CellBuilder
import org.ton.kotlin.cell.CellContext
import org.ton.kotlin.cell.CellSlice
import org.ton.kotlin.cell.serialization.CellSerializer
import org.ton.kotlin.currency.Coins

/**
 * Storage profile of an account.
 */
public data class StorageInfo(
    /**
     * Amount of unique cells and bits which account state occupies.
     */
    val used: StorageUsed,

    /**
     * Unix timestamp of the last storage phase.
     */
    val lastPaid: Long,

    /**
     * Account debt for storing its state.
     */
    val duePayment: Coins?
)

private object StorageInfoSerializer : CellSerializer<StorageInfo> {
    override fun load(
        slice: CellSlice,
        context: CellContext
    ): StorageInfo {
        TODO("Not yet implemented")
    }

    override fun store(
        builder: CellBuilder,
        value: StorageInfo,
        context: CellContext
    ) {
        TODO("Not yet implemented")
    }

}

//public object Tlb : TlbCodec<StorageInfo> {
//    override fun storeTlb(
//        cellBuilder: CellBuilder, value: StorageInfo
//    ): Unit = cellBuilder {
//        storeTlb(StorageUsed.Tlb, value.used)
//        storeUInt32(value.lastPaid.toUInt())
//        storeNullableTlb(Coins.Tlb, value.duePayment)
//    }
//
//    override fun loadTlb(
//        cellSlice: CellSlice
//    ): StorageInfo = cellSlice {
//        val used = loadTlb(StorageUsed.Tlb)
//        val lastPaid = loadUInt().toLong()
//        val duePayment = loadNullableTlb(Coins.Tlb)
//        StorageInfo(used, lastPaid, duePayment)
//    }
//}