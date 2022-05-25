package org.ton.block.tlb

import org.ton.block.Coins
import org.ton.block.CurrencyCollection
import org.ton.block.ImportFees
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

fun ImportFees.Companion.tlbCodec(): TlbCodec<ImportFees> = ImportFeesTlbConstructor()

private class ImportFeesTlbConstructor : TlbConstructor<ImportFees>(
    schema = "import_fees\$_ fees_collected:Coins value_imported:CurrencyCollection = ImportFees;"
) {
    private val coinsCodec by lazy {
        Coins.tlbCodec()
    }
    private val currencyCollectionCodec by lazy {
        CurrencyCollection.tlbCodec()
    }

    override fun encode(
        cellBuilder: CellBuilder, value: ImportFees, param: Int, negativeParam: (Int) -> Unit
    ) = cellBuilder {
        storeTlb(value.feesCollected, coinsCodec)
        storeTlb(value.valueImported, currencyCollectionCodec)
    }

    override fun decode(
        cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit
    ): ImportFees = cellSlice {
        val feesCollected = loadTlb(coinsCodec)
        val valueImported = loadTlb(currencyCollectionCodec)
        ImportFees(feesCollected, valueImported)
    }
}
