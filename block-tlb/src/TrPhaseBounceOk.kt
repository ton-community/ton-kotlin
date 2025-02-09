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


@SerialName("tr_phase_bounce_ok")
public data class TrPhaseBounceOk(
    val msgSize: StorageUsedShort,
    val msgFees: Coins,
    val fwdFees: Coins
) : TrBouncePhase {
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
        type("tr_phase_bounce_ok") {
            field("msg_size", msgSize)
            field("msg_fees", msgFees)
            field("fwd_fees", fwdFees)
        }
    }

    override fun toString(): String = print().toString()

    public companion object : TlbConstructorProvider<TrPhaseBounceOk> by TrPhaseBounceOkTlbConstructor
}

private object TrPhaseBounceOkTlbConstructor : TlbConstructor<TrPhaseBounceOk>(
    schema = "tr_phase_bounce_ok\$1 msg_size:StorageUsedShort msg_fees:Coins fwd_fees:Coins = TrBouncePhase;"
) {

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: TrPhaseBounceOk
    ) = cellBuilder {
        storeTlb(StorageUsedShort, value.msgSize)
        storeTlb(Coins, value.msgFees)
        storeTlb(Coins, value.fwdFees)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): TrPhaseBounceOk = cellSlice {
        val msgSize = loadTlb(StorageUsedShort)
        val msgFees = loadTlb(Coins)
        val fwdFees = loadTlb(Coins)
        TrPhaseBounceOk(msgSize, msgFees, fwdFees)
    }
}
