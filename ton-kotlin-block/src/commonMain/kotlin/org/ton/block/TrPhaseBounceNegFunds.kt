package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

@Serializable
@SerialName("tr_phase_bounce_negfunds")
object TrPhaseBounceNegFunds : TrBouncePhase,
    TlbConstructorProvider<TrPhaseBounceNegFunds> by TrPhaseBounceNegFundsTlbConstructor {
}

private object TrPhaseBounceNegFundsTlbConstructor : TlbConstructor<TrPhaseBounceNegFunds>(
    schema = "tr_phase_bounce_negfunds\$00 = TrBouncePhase;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: TrPhaseBounceNegFunds) = Unit
    override fun loadTlb(cellSlice: CellSlice): TrPhaseBounceNegFunds = TrPhaseBounceNegFunds
}
