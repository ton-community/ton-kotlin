package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.*
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

@SerialName("tr_phase_credit")
@Serializable
public data class TrCreditPhase(
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

    public companion object : TlbConstructorProvider<TrCreditPhase> by TrCreditPhaseTlbConstructor
}

private object TrCreditPhaseTlbConstructor : TlbConstructor<TrCreditPhase>(
    schema = "tr_phase_credit\$_ due_fees_collected:(Maybe Coins) credit:CurrencyCollection = TrCreditPhase;"
) {
    val maybeCoins = Maybe.tlbCodec(Coins)

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: TrCreditPhase
    ) = cellBuilder {
        storeTlb(maybeCoins, value.dueFeesCollected)
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
