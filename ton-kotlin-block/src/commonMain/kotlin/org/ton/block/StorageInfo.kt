package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.providers.TlbConstructorProvider
import org.ton.tlb.storeTlb

@SerialName("storage_info")
@Serializable
data class StorageInfo(
    val used: StorageUsed,
    val last_paid: UInt,
    val due_payment: Maybe<Coins>
) {
    constructor(used: StorageUsed, lastPaid: UInt, duePayment: Coins? = null) : this(
        used,
        lastPaid,
        duePayment.toMaybe()
    )

    override fun toString(): String = "storage_info(used:$used last_paid:$last_paid due_payment:$due_payment)"

    companion object : TlbConstructorProvider<StorageInfo> by StorageInfoTlbConstructor
}

private object StorageInfoTlbConstructor : TlbConstructor<StorageInfo>(
    schema = "storage_info\$_ used:StorageUsed last_paid:uint32 due_payment:(Maybe Coins) = StorageInfo;"
) {
    private val maybeCoins = Maybe(Coins)

    override fun storeTlb(
        cellBuilder: CellBuilder, value: StorageInfo
    ) = cellBuilder {
        storeTlb(StorageUsed, value.used)
        storeUInt32(value.last_paid)
        storeTlb(maybeCoins, value.due_payment)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): StorageInfo = cellSlice {
        val used = loadTlb(StorageUsed)
        val lastPaid = loadUInt32()
        val duePayment = loadTlb(maybeCoins)
        StorageInfo(used, lastPaid, duePayment)
    }
}
