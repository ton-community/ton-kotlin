@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbObject
import org.ton.tlb.providers.TlbCombinatorProvider

@JsonClassDiscriminator("@type")

public sealed interface TransactionDescr : TlbObject {
    public companion object : TlbCombinatorProvider<TransactionDescr> by TransactionDescrTlbCombinator
}

private object TransactionDescrTlbCombinator : TlbCombinator<TransactionDescr>(
    TransactionDescr::class,
    TransOrd::class to TransOrd.tlbConstructor(),
    TransStorage::class to TransStorage.tlbConstructor(),
    TransTickTock::class to TransTickTock.tlbConstructor(),
    TransMergeInstall::class to TransMergeInstall.tlbConstructor(),
    TransMergePrepare::class to TransMergePrepare.tlbConstructor(),
    TransSplitInstall::class to TransSplitInstall.tlbConstructor(),
    TransSplitPrepare::class to TransSplitPrepare.tlbConstructor(),
)
