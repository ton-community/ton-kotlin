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

public data class ExecutedBouncePhase(
    val msgSize: StorageUsedShort,
    val msgFees: Coins,
    val fwdFees: Coins
) : BouncePhase {
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
        type("tr_phase_bounce_ok") {
            field("msg_size", msgSize)
            field("msg_fees", msgFees)
            field("fwd_fees", fwdFees)
        }
    }

    override fun toString(): String = print().toString()

    public companion object : TlbConstructorProvider<ExecutedBouncePhase> by TrPhaseBounceOkTlbConstructor
}

private object TrPhaseBounceOkTlbConstructor : TlbConstructor<ExecutedBouncePhase>(
    schema = "tr_phase_bounce_ok\$1 msg_size:StorageUsedShort msg_fees:Coins fwd_fees:Coins = TrBouncePhase;"
) {

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: ExecutedBouncePhase
    ) = cellBuilder {
        storeTlb(StorageUsedShort.Tlb, value.msgSize)
        storeTlb(Coins.Tlb, value.msgFees)
        storeTlb(Coins.Tlb, value.fwdFees)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): ExecutedBouncePhase = cellSlice {
        val msgSize = loadTlb(StorageUsedShort.Tlb)
        val msgFees = loadTlb(Coins.Tlb)
        val fwdFees = loadTlb(Coins.Tlb)
        ExecutedBouncePhase(msgSize, msgFees, fwdFees)
    }
}
