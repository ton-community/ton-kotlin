@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbCombinatorProvider

@JsonClassDiscriminator("@type")
@Serializable
sealed interface TransactionDescr {
    companion object : TlbCombinatorProvider<TransactionDescr> by TransactionDescrTlbCombinator
}

private object TransactionDescrTlbCombinator : TlbCombinator<TransactionDescr>() {
    val ord = TransOrd.tlbConstructor()
    val storage = TransStorage.tlbConstructor()
    val tickTock = TransTickTock.tlbConstructor()
    val mergeInstall = TransMergeInstall.tlbConstructor()
    val mergePrepare = TransMergePrepare.tlbConstructor()
    val splitInstall = TransSplitInstall.tlbConstructor()
    val splitPrepare = TransSplitPrepare.tlbConstructor()

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
