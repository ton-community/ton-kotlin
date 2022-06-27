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
    val ord by lazy { TransOrd.tlbCodec() }
    val storage by lazy { TransStorage.tlbCodec() }
    val tickTock by lazy { TransTickTock.tlbCodec() }
    val mergeInstall by lazy { TransMergeInstall.tlbCodec() }
    val mergePrepare by lazy { TransMergePrepare.tlbCodec() }
    val splitInstall by lazy { TransSplitInstall.tlbCodec() }
    val splitPrepare by lazy { TransSplitPrepare.tlbCodec() }

    override val constructors: List<TlbConstructor<out TransactionDescr>> by lazy {
        listOf(ord, storage, tickTock, mergeInstall, mergePrepare, splitInstall, splitPrepare)
    }

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
