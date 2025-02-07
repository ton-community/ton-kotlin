package org.ton.block

import kotlinx.serialization.SerialName
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbConstructor
import org.ton.tlb.TlbPrettyPrinter
import org.ton.tlb.loadTlb
import org.ton.tlb.providers.TlbConstructorProvider
import org.ton.tlb.storeTlb


@SerialName("tr_phase_bounce_nofunds")
public data class TrPhaseBounceNoFunds(
    val msgSize: StorageUsedShort,
    val reqFwdFees: Coins
) : TrBouncePhase {
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
        type("tr_phase_bounce_nofunds") {
            field("msg_size", msgSize)
            field("reqFwdFees", reqFwdFees)
        }
    }

    override fun toString(): String = print().toString()

    public companion object : TlbConstructorProvider<TrPhaseBounceNoFunds> by TrPhaseBounceNoFundsTlbConstructor
}

private object TrPhaseBounceNoFundsTlbConstructor : TlbConstructor<TrPhaseBounceNoFunds>(
    schema = "tr_phase_bounce_nofunds\$01 msg_size:StorageUsedShort req_fwd_fees:Coins = TrBouncePhase;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: TrPhaseBounceNoFunds
    ) = cellBuilder {
        storeTlb(StorageUsedShort, value.msgSize)
        storeTlb(Coins, value.reqFwdFees)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): TrPhaseBounceNoFunds = cellSlice {
        val msgSize = loadTlb(StorageUsedShort)
        val coins = loadTlb(Coins)
        TrPhaseBounceNoFunds(msgSize, coins)
    }
}
