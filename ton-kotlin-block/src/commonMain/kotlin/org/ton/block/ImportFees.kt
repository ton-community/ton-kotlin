package org.ton.block

import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.providers.TlbConstructorProvider
import org.ton.tlb.storeTlb

@Serializable
data class ImportFees(
    val fees_collected: Coins,
    val value_imported: CurrencyCollection
) {
    companion object : TlbConstructorProvider<ImportFees> by ImportFeesTlbConstructor
}

private object ImportFeesTlbConstructor : TlbConstructor<ImportFees>(
    schema = "import_fees\$_ fees_collected:Coins value_imported:CurrencyCollection = ImportFees;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder, value: ImportFees
    ) = cellBuilder {
        storeTlb(Coins, value.fees_collected)
        storeTlb(CurrencyCollection, value.value_imported)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): ImportFees = cellSlice {
        val feesCollected = loadTlb(Coins)
        val valueImported = loadTlb(CurrencyCollection)
        ImportFees(feesCollected, valueImported)
    }
}
