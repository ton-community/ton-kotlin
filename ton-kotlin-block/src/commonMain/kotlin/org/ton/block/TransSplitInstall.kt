package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.cell.storeRef
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.providers.TlbConstructorProvider
import org.ton.tlb.storeTlb

@Serializable
@SerialName("trans_split_install")
data class TransSplitInstall(
    val split_info: SplitMergeInfo,
    val prepare_transaction: Transaction,
    val installed: Boolean
) : TransactionDescr {
    companion object : TlbConstructorProvider<TransSplitInstall> by TransSplitInstallTlbConstructor
}

private object TransSplitInstallTlbConstructor : TlbConstructor<TransSplitInstall>(
    schema = "trans_split_install\$0101 split_info:SplitMergeInfo\n" +
        "  prepare_transaction:^Transaction\n" +
        "  installed:Bool = TransactionDescr;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: TransSplitInstall
    ) = cellBuilder {
        storeTlb(SplitMergeInfo, value.split_info)
        storeRef { storeTlb(Transaction, value.prepare_transaction) }
        storeBit(value.installed)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): TransSplitInstall = cellSlice {
        val splitInfo = loadTlb(SplitMergeInfo)
        val prepareTransaction = loadTlb(Transaction)
        val installed = loadBit()
        TransSplitInstall(splitInfo, prepareTransaction, installed)
    }
}
