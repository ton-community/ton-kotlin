package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.*
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@SerialName("storage_info")
@Serializable
data class StorageInfo(
    val used: StorageUsed,
    @SerialName("last_paid")
    val lastPaid: Int,
    @SerialName("due_payment")
    val duePayment: Maybe<Coins>
) {
    constructor(used: StorageUsed, lastPaid: Int, duePayment: Coins? = null) : this(
        used,
        lastPaid,
        duePayment.toMaybe()
    )

    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<StorageInfo> = StorageInfoTlbConstructor()
    }
}

private class StorageInfoTlbConstructor : TlbConstructor<StorageInfo>(
    schema = "storage_info\$_ used:StorageUsed last_paid:uint32 due_payment:(Maybe Coins) = StorageInfo;"
) {
    private val storageUsedCodec by lazy {
        StorageUsed.tlbCodec()
    }
    private val maybeCoins by lazy {
        Maybe.tlbCodec(Coins.tlbCodec())
    }

    override fun storeTlb(
        cellBuilder: CellBuilder, value: StorageInfo
    ) = cellBuilder {
        storeTlb(storageUsedCodec, value.used)
        storeUInt(value.lastPaid, 32)
        storeTlb(maybeCoins, value.duePayment)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): StorageInfo = cellSlice {
        val used = loadTlb(storageUsedCodec)
        val lastPaid = loadUInt(32).toInt()
        val duePayment = loadTlb(maybeCoins)
        StorageInfo(used, lastPaid, duePayment)
    }
}
