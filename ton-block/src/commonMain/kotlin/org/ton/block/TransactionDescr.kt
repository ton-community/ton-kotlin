@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor

@JsonClassDiscriminator("@type")
@Serializable
sealed interface TransactionDescr {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbCombinator<TransactionDescr> = TransactionDescrTlbCombinator
    }
}

private object TransactionDescrTlbCombinator : TlbCombinator<TransactionDescr>() {
    val ord = TransOrd.tlbCodec()
    val storage = TransStorage.tlbCodec()
    val tickTock = TransTickTock.tlbCodec()
    val mergeInstall = TransMergeInstall.tlbCodec()
    val mergePrepare = TransMergePrepare.tlbCodec()
    val splitInstall = TransSplitInstall.tlbCodec()
    val splitPrepare = TransSplitPrepare.tlbCodec()

    override val constructors: List<TlbConstructor<out TransactionDescr>> =
        listOf(ord, storage, tickTock, mergeInstall, mergePrepare, splitInstall, splitPrepare)

    override fun getConstructor(
        value: TransactionDescr
    ): TlbConstructor<out TransactionDescr> = when (value) {
        is TransOrd -> ord
        is TransStorage -> storage
        is TransTickTock -> tickTock
        is TransMergeInstall -> mergeInstall
        is TransMergePrepare -> mergePrepare
        is TransSplitInstall -> splitInstall
        is TransSplitPrepare -> splitPrepare
    }
}
