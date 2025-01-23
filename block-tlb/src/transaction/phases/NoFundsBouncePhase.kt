package org.ton.block.transaction.phases

import org.ton.block.account.StorageUsedShort
import org.ton.block.currency.Coins
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbConstructor
import org.ton.tlb.TlbPrettyPrinter
import org.ton.tlb.loadTlb
import org.ton.tlb.providers.TlbConstructorProvider
import org.ton.tlb.storeTlb

public data class NoFundsBouncePhase(
    val msgSize: StorageUsedShort,
    val reqFwdFees: Coins
) : BouncePhase {
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
        type("tr_phase_bounce_nofunds") {
            field("msg_size", msgSize)
            field("reqFwdFees", reqFwdFees)
        }
    }

    override fun toString(): String = print().toString()

    public companion object : TlbConstructorProvider<NoFundsBouncePhase> by TrPhaseBounceNoFundsTlbConstructor
}

private object TrPhaseBounceNoFundsTlbConstructor : TlbConstructor<NoFundsBouncePhase>(
    schema = "tr_phase_bounce_nofunds\$01 msg_size:StorageUsedShort req_fwd_fees:Coins = TrBouncePhase;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: NoFundsBouncePhase
    ) = cellBuilder {
        storeTlb(StorageUsedShort.Tlb, value.msgSize)
        storeTlb(Coins.Tlb, value.reqFwdFees)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): NoFundsBouncePhase = cellSlice {
        val msgSize = loadTlb(StorageUsedShort.Tlb)
        val coins = loadTlb(Coins.Tlb)
        NoFundsBouncePhase(msgSize, coins)
    }
}
