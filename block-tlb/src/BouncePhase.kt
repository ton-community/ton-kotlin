package org.ton.block

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCodec
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

/**
 * Bounce phase info.
 *
 * At this phase, some funds are returned to the sender.
 *
 * @see [TransactionInfo]
 */
public sealed interface BouncePhase {
    public val forwardFees: Coins

    /**
     * Skipped bounce phase info.
     */
    public data class NoFunds(
        /**
         * The total number of unique cells (bits / refs) of the bounced message.
         */
        val msgSize: StorageUsedShort,

        /**
         * Required amount of coins to send the bounced message.
         */
        override val forwardFees: Coins
    ) : BouncePhase

    /**
     * Bounce phase was executed.
     */
    public data class Executed(
        /**
         * The total number of unique cells (bits / refs) of the bounced message.
         */
        val msgSize: StorageUsedShort = StorageUsedShort.ZERO,

        /**
         * The part of fees for the validators.
         */
        val forwardFeesCollected: Coins = Coins.ZERO,

        /**
         * Message forwarding fee.
         */
        override val forwardFees: Coins = Coins.ZERO,
    ) : BouncePhase

    public companion object : TlbCodec<BouncePhase> by BouncePhaseCodec
}

private object BouncePhaseCodec : TlbCodec<BouncePhase> {
    private val ZERO = BouncePhase.Executed()

    override fun storeTlb(builder: CellBuilder, value: BouncePhase) {
        when (value) {
            is BouncePhase.Executed -> { // tr_phase_bounce_ok$1
                builder.storeBoolean(true)
                builder.storeTlb(StorageUsedShort, value.msgSize) // msg_size:StorageUsed
                builder.storeTlb(Coins, value.forwardFeesCollected) // msg_fees:Coins
                builder.storeTlb(Coins, value.forwardFees) // fwd_fees:Coins
            }

            is BouncePhase.NoFunds -> { // tr_phase_bounce_nofunds$01
                builder.storeUInt(0b01, 2)
                builder.storeTlb(StorageUsedShort, value.msgSize) // msg_size:StorageUsed
                builder.storeTlb(Coins, value.forwardFees) // req_fwd_fees:Coins
            }
        }
    }

    override fun loadTlb(slice: CellSlice): BouncePhase {
        when (val tag = slice.preloadUInt(2).toInt()) {
            0b00 -> {
                slice.skipBits(2)
                return ZERO
            }

            0b01 -> { // tr_phase_bounce_nofunds$01
                slice.skipBits(2)
                val msgSize = slice.loadTlb(StorageUsedShort)
                val forwardFees = slice.loadTlb(Coins)
                return BouncePhase.NoFunds(msgSize, forwardFees)
            }

            0b10, 0b11 -> { // tr_phase_bounce_ok$1
                slice.skipBits(1)
                val msgSize = slice.loadTlb(StorageUsedShort)
                val forwardFees = slice.loadTlb(Coins)
                return BouncePhase.Executed(msgSize, forwardFees)
            }

            else -> throw IllegalArgumentException("Invalid bounce phase tag: ${tag.toString(2)}")
        }
    }

}