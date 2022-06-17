package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@Serializable
data class ImportFees(
    @SerialName("fees_collected")
    val feesCollected: Coins,
    @SerialName("value_imported")
    val valueImported: CurrencyCollection
) {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<ImportFees> = ImportFeesTlbConstructor()
    }
}

private class ImportFeesTlbConstructor : TlbConstructor<ImportFees>(
    schema = "import_fees\$_ fees_collected:Coins value_imported:CurrencyCollection = ImportFees;"
) {
    private val coinsCodec by lazy {
        Coins.tlbCodec()
    }
    private val currencyCollectionCodec by lazy {
        CurrencyCollection.tlbCodec()
    }

    override fun storeTlb(
        cellBuilder: CellBuilder, value: ImportFees
    ) = cellBuilder {
        storeTlb(coinsCodec, value.feesCollected)
        storeTlb(currencyCollectionCodec, value.valueImported)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): ImportFees = cellSlice {
        val feesCollected = loadTlb(coinsCodec)
        val valueImported = loadTlb(currencyCollectionCodec)
        ImportFees(feesCollected, valueImported)
    }
}
