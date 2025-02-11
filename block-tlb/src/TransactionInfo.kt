@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.storeRef
import org.ton.tlb.*

/**
 * Detailed transaction info.
 *
 * @see [Transaction]
 */
public sealed interface TransactionInfo {
    /**
     * Storage phase info.
     */
    public val storagePhase: StoragePhase?

    /**
     * Credit phase info.
     */
    public val creditPhase: CreditPhase?

    /**
     * Compute phase info.
     */
    public val computePhase: ComputePhase?

    /**
     * Action phase info.
     */
    public val actionPhase: ActionPhase?

    /**
     * Bounce phase info.
     *
     * Only present in [Ordinary] transaction and if the incoming [Message] had `bounce: true` and
     * the compute phase failed.
     */
    public val bouncePhase: BouncePhase?

    /**
     * Ordinary transaction info.
     */
    public data class Ordinary(
        /**
         * Whether the credit phase was executed first
         *
         * (usually set when incoming message has `bounce: false`).
         */
        val isCreditFirst: Boolean,

        /**
         * Storage phase info.
         *
         * Skipped if the account did not exist prior to execution.
         */
        override val storagePhase: StoragePhase?,

        /**
         * Credit phase info.
         *
         * Skipped if the incoming message is external.
         */
        override val creditPhase: CreditPhase?,

        /**
         * Compute phase info.
         */
        override val computePhase: ComputePhase,

        /**
         * Action phase info.
         *
         * Skipped if the transaction was aborted at the compute phase.
         */
        override val actionPhase: ActionPhase?,

        /**
         * Whether the transaction was reverted.
         */
        val isAborted: Boolean,

        /**
         * Bounce phase info.
         *
         * Only present if the incoming message had `bounce: true` and
         * the compute phase failed.
         */
        override val bouncePhase: BouncePhase?,

        /**
         * Whether the account was destroyed during this transaction.
         */
        val isDestroyed: Boolean
    ) : TransactionInfo

    /**
     * Tick-Tock transaction info
     */
    public data class TickTock(
        val isTock: Boolean,

        /**
         * Storage phase info.
         */
        override val storagePhase: StoragePhase,

        /**
         * Compute phase info.
         */
        override val computePhase: ComputePhase,

        /**
         * Action phase info.
         *
         * Skipped if the transaction was aborted at the compute phase.
         */
        override val actionPhase: ActionPhase?,

        /**
         * Whether the transaction was reverted.
         */
        val isAborted: Boolean,

        /**
         * Whether the account was destroyed during this transaction.
         */
        val isDestroyed: Boolean
    ) : TransactionInfo {
        val isTick: Boolean get() = !isTock
        override val creditPhase: CreditPhase? get() = null
        override val bouncePhase: BouncePhase? get() = null
    }

    /**
     * Storage transaction info
     *
     * **Currently, not implemented in TON Blockchain**
     */
    @Deprecated("Not implemented in TON Blockchain")
    public data class Storage(
        /**
         * Storage phase info.
         */
        override val storagePhase: StoragePhase,
    ) : TransactionInfo {
        override val creditPhase: CreditPhase? get() = null
        override val computePhase: ComputePhase? get() = null
        override val actionPhase: ActionPhase? get() = null
        override val bouncePhase: BouncePhase? get() = null
    }

    /**
     * Split prepare transaction info.
     *
     * **Currently, not implemented in TON Blockchain**
     */
    @Deprecated("Not implemented in TON Blockchain")
    public data class SplitPrepare(
        val splitInfo: SplitMergeInfo,
        override val storagePhase: StoragePhase?,
        override val computePhase: ComputePhase,
        override val actionPhase: ActionPhase?,
        val isAborted: Boolean,
        val isDestroyed: Boolean
    ) : TransactionInfo {
        override val creditPhase: CreditPhase? get() = null
        override val bouncePhase: BouncePhase? get() = null
    }

    /**
     * Split install transaction info.
     *
     * **Currently, not implemented in TON Blockchain**
     */
    @Deprecated("Not implemented in TON Blockchain")
    public data class SplitInstall(
        val splitInfo: SplitMergeInfo,
        val prepareTransaction: CellRef<Transaction>,
        val isInstalled: Boolean,
    ) : TransactionInfo {
        override val storagePhase: StoragePhase? get() = null
        override val creditPhase: CreditPhase? get() = null
        override val computePhase: ComputePhase? get() = null
        override val actionPhase: ActionPhase? get() = null
        override val bouncePhase: BouncePhase? get() = null
    }

    /**
     * Merge-prepare transaction info.
     *
     * **Currently, not implemented in TON Blockchain**
     */
    @Deprecated("Not implemented in TON Blockchain")
    public data class MergePrepare(
        val splitInfo: SplitMergeInfo,
        override val storagePhase: StoragePhase,
        val isAborted: Boolean,
    ) : TransactionInfo {
        override val creditPhase: CreditPhase? get() = null
        override val computePhase: ComputePhase? get() = null
        override val actionPhase: ActionPhase? get() = null
        override val bouncePhase: BouncePhase? get() = null
    }

    /**
     * Merge-install transaction info.
     *
     * **Currently, not implemented in TON Blockchain**
     */
    @Deprecated("Not implemented in TON Blockchain")
    public data class MergeInstall(
        val splitInfo: SplitMergeInfo,
        val prepareTransaction: CellRef<Transaction>,
        override val storagePhase: StoragePhase?,
        override val creditPhase: CreditPhase?,
        override val computePhase: ComputePhase,
        override val actionPhase: ActionPhase?,
        val isAborted: Boolean,
        val isDestroyed: Boolean
    ) : TransactionInfo {
        override val bouncePhase: BouncePhase? get() = null
    }

    public companion object : TlbCodec<TransactionInfo> by TransactionInfoCodec
}

private object TransactionInfoCodec : TlbCodec<TransactionInfo> {
    private val maybeStoragePhase = StoragePhase.asNullable()
    private val maybeCreditPhase = CreditPhase.asNullable()
    private val maybeRefActionPhase = CellRef.tlbCodec(ActionPhase).asNullable()
    private val maybeBouncePhase = BouncePhase.asNullable()

    @Suppress("DEPRECATION")
    override fun storeTlb(builder: CellBuilder, value: TransactionInfo) {
        when (value) {
            is TransactionInfo.Ordinary -> { // trans_ord$0000
                builder.storeUInt(0b0000, 4)
                builder.storeBoolean(value.isCreditFirst)
                builder.storeTlb(maybeStoragePhase, value.storagePhase)
                builder.storeTlb(maybeCreditPhase, value.creditPhase)
                builder.storeTlb(ComputePhase, value.computePhase)
                val actionPhase = value.actionPhase
                if (actionPhase != null) {
                    builder.storeBoolean(true)
                    builder.storeRef {
                        storeTlb(ActionPhase, actionPhase)
                    }
                } else {
                    builder.storeBoolean(false)
                }
                builder.storeBoolean(value.isAborted)
                builder.storeTlb(maybeBouncePhase, value.bouncePhase)
                builder.storeBoolean(value.isDestroyed)
            }

            is TransactionInfo.TickTock -> TODO()
            is TransactionInfo.MergeInstall -> TODO()
            is TransactionInfo.MergePrepare -> TODO()
            is TransactionInfo.SplitInstall -> TODO()
            is TransactionInfo.SplitPrepare -> TODO()
            is TransactionInfo.Storage -> TODO()
        }
    }

    @Suppress("DEPRECATION")
    override fun loadTlb(slice: CellSlice): TransactionInfo {
        when (val tag = getTag(slice)) {
            0b0000 -> { // trans_ord$0000
                slice.bitsPosition += 4
                val isCreditFirst = slice.loadBoolean()
                val storagePhase = slice.loadTlb(maybeStoragePhase)
                val creditPhase = slice.loadTlb(maybeCreditPhase)
                val computePhase = slice.loadTlb(ComputePhase)
                val actionPhase = slice.loadTlb(maybeRefActionPhase)?.load()
                val isAborted = slice.loadBoolean()
                val bouncePhase = slice.loadTlb(maybeBouncePhase)
                println("bounce: $bouncePhase")
                val isDestroyed = slice.loadBoolean()
                return TransactionInfo.Ordinary(
                    isCreditFirst,
                    storagePhase,
                    creditPhase,
                    computePhase,
                    actionPhase,
                    isAborted,
                    bouncePhase,
                    isDestroyed
                )
            }

            0b0001 -> { // trans_storage$0001
                slice.bitsPosition += 4
                val storagePhase = slice.loadTlb(StoragePhase)
                return TransactionInfo.Storage(storagePhase)
            }

            0b0010 -> { // trans_tick_tock$001
                slice.bitsPosition += 3
                val isTock = slice.loadBoolean()
                val storagePhase = slice.loadTlb(StoragePhase)
                val computePhase = slice.loadTlb(ComputePhase)
                val actionPhase = slice.loadTlb(maybeRefActionPhase)?.load()
                val isAborted = slice.loadBoolean()
                val isDestroyed = slice.loadBoolean()
                return TransactionInfo.TickTock(isTock, storagePhase, computePhase, actionPhase, isAborted, isDestroyed)
            }

            0b0100 -> { // trans_split_prepare$0100
                slice.bitsPosition += 4
                val splitInfo = slice.loadTlb(SplitMergeInfo)
                val storagePhase = slice.loadTlb(maybeStoragePhase)
                val computePhase = slice.loadTlb(ComputePhase)
                val actionPhase = slice.loadTlb(maybeRefActionPhase)?.load()
                val isAborted = slice.loadBoolean()
                val isDestroyed = slice.loadBoolean()
                return TransactionInfo.SplitPrepare(
                    splitInfo,
                    storagePhase,
                    computePhase,
                    actionPhase,
                    isAborted,
                    isDestroyed
                )
            }

            0b0101 -> { // trans_split_install$0101
                slice.bitsPosition += 4
                val splitInfo = slice.loadTlb(SplitMergeInfo)
                val prepareTransaction = CellRef(slice.loadRef(), Transaction)
                val isInstalled = slice.loadBoolean()
                return TransactionInfo.SplitInstall(splitInfo, prepareTransaction, isInstalled)
            }

            0b0110 -> { // trans_merge_prepare$0110
                slice.bitsPosition += 4
                val splitInfo = slice.loadTlb(SplitMergeInfo)
                val storagePhase = slice.loadTlb(StoragePhase)
                val isAborted = slice.loadBoolean()
                return TransactionInfo.MergePrepare(splitInfo, storagePhase, isAborted)
            }

            0b0111 -> { // trans_merge_install$0111
                slice.bitsPosition += 4
                val splitInfo = slice.loadTlb(SplitMergeInfo)
                val prepareTransaction = CellRef(slice.loadRef(), Transaction)
                val storagePhase = slice.loadTlb(maybeStoragePhase)
                val creditPhase = slice.loadTlb(maybeCreditPhase)
                val computePhase = slice.loadTlb(ComputePhase)
                val actionPhase = slice.loadTlb(maybeRefActionPhase)?.load()
                val isAborted = slice.loadBoolean()
                val isDestroyed = slice.loadBoolean()
                return TransactionInfo.MergeInstall(
                    splitInfo,
                    prepareTransaction,
                    storagePhase,
                    creditPhase,
                    computePhase,
                    actionPhase,
                    isAborted,
                    isDestroyed
                )
            }

            else -> throw IllegalArgumentException("Invalid transaction info tag: $tag")
        }
    }

    private fun getTag(slice: CellSlice): Int = slice.bitSelect(4, 0xF7)

    private fun CellSlice.bitSelect(bitCount: Int, mask: Int): Int {
        val n = preloadUInt(bitCount).toInt()
        return (mask and ((0b10 shl n) - 1)).countOneBits() - 1
    }
}