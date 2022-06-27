package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.*
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@SerialName("tr_phase_credit")
@Serializable
data class TrCreditPhase(
    val due_fees_collected: Maybe<Coins>,
    val credit: CurrencyCollection
) {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<TrCreditPhase> = TrCreditPhaseTlbConstructor
    }
}

private object TrCreditPhaseTlbConstructor : TlbConstructor<TrCreditPhase>(
    schema = "tr_phase_credit\$_ due_fees_collected:(Maybe Coins) credit:CurrencyCollection = TrCreditPhase;"
) {
    val maybeCoins by lazy {
        Maybe.tlbCodec(Coins.tlbCodec())
    }
    val currencyCollection by lazy {
        CurrencyCollection.tlbCodec()
    }

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: TrCreditPhase
    ) = cellBuilder {
        storeTlb(maybeCoins, value.due_fees_collected)
        storeTlb(currencyCollection, value.credit)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): TrCreditPhase = cellSlice {
        val dueFeesCollected = loadTlb(maybeCoins)
        val credit = loadTlb(currencyCollection)
        TrCreditPhase(dueFeesCollected, credit)
    }
}