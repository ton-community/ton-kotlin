package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.*
import org.ton.tlb.providers.TlbConstructorProvider

@Serializable
@SerialName("trans_split_install")
public data class TransSplitInstall(
    @SerialName("split_info") val splitInfo: SplitMergeInfo,
    @SerialName("prepare_transaction") val prepareTransaction: CellRef<Transaction>,
    val installed: Boolean
) : TransactionDescr {
    public companion object : TlbConstructorProvider<TransSplitInstall> by TransSplitInstallTlbConstructor

    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter {
        return printer {
            type("trans_split_install") {
                field("split_info", splitInfo)
                field("prepare_transaction", prepareTransaction)
                field("installed", installed)
            }
        }
    }

    override fun toString(): String = print().toString()
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
        storeTlb(SplitMergeInfo, value.splitInfo)
        storeRef(Transaction, value.prepareTransaction)
        storeBit(value.installed)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): TransSplitInstall = cellSlice {
        val splitInfo = loadTlb(SplitMergeInfo)
        val prepareTransaction = loadRef(Transaction)
        val installed = loadBit()
        TransSplitInstall(splitInfo, prepareTransaction, installed)
    }
}
