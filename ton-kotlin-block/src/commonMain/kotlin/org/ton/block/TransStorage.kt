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

@Serializable
@SerialName("trans_storage")
data class TransStorage(
    val storage_ph: TrStoragePhase
) : TransactionDescr {
    companion object : TlbConstructorProvider<TransStorage> by TransStorageTlbConstructor
}

private object TransStorageTlbConstructor : TlbConstructor<TransStorage>(
    schema = "trans_storage\$0001 storage_ph:TrStoragePhase = TransactionDescr;"
) {

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: TransStorage
    ) = cellBuilder {
        storeTlb(TrStoragePhase, value.storage_ph)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): TransStorage = cellSlice {
        val storagePh = loadTlb(TrStoragePhase)
        TransStorage(storagePh)
    }
}
