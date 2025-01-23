package org.ton.block.transaction.phases

import kotlinx.serialization.SerialName
import org.ton.block.Maybe
import org.ton.block.currency.Coins
import org.ton.block.currency.CurrencyCollection
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.*
import org.ton.tlb.providers.TlbConstructorProvider

public data class CreditPhase(
    @SerialName("due_fees_collected") val dueFeesCollected: Maybe<Coins>,
    val credit: CurrencyCollection
) : TlbObject {
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
        type("tr_phase_credit") {
            field("due_fees_collected", dueFeesCollected)
            field("credit", credit)
        }
    }

    override fun toString(): String = print().toString()

    public companion object : TlbConstructorProvider<CreditPhase> by TrCreditPhaseTlbConstructor
}

private object TrCreditPhaseTlbConstructor : TlbConstructor<CreditPhase>(
    schema = "tr_phase_credit\$_ due_fees_collected:(Maybe Coins) credit:CurrencyCollection = TrCreditPhase;"
) {
    val maybeCoins = Maybe.Companion.tlbCodec(Coins.Tlb)

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: CreditPhase
    ) = cellBuilder {
        storeTlb(maybeCoins, value.dueFeesCollected)
        storeTlb(CurrencyCollection.Tlb, value.credit)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): CreditPhase = cellSlice {
        val dueFeesCollected = loadTlb(maybeCoins)
        val credit = loadTlb(CurrencyCollection.Tlb)
        CreditPhase(dueFeesCollected, credit)
    }
}
