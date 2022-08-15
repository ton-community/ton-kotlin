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

@SerialName("tr_phase_credit")
@Serializable
data class TrCreditPhase(
    val due_fees_collected: Maybe<Coins>,
    val credit: CurrencyCollection
) {
    companion object : TlbConstructorProvider<TrCreditPhase> by TrCreditPhaseTlbConstructor

    override fun toString(): String = buildString {
        append("(tr_phase_credit\ndue_fees_collected:")
        append(due_fees_collected)
        append(" credit:")
        append(credit)
        append(")")
    }
}

private object TrCreditPhaseTlbConstructor : TlbConstructor<TrCreditPhase>(
    schema = "tr_phase_credit\$_ due_fees_collected:(Maybe Coins) credit:CurrencyCollection = TrCreditPhase;"
) {
    val maybeCoins = Maybe.tlbCodec(Coins)

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: TrCreditPhase
    ) = cellBuilder {
        storeTlb(maybeCoins, value.due_fees_collected)
        storeTlb(CurrencyCollection, value.credit)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): TrCreditPhase = cellSlice {
        val dueFeesCollected = loadTlb(maybeCoins)
        val credit = loadTlb(CurrencyCollection)
        TrCreditPhase(dueFeesCollected, credit)
    }
}
