package org.ton.tonlib.block

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.ensureActive

internal data class BlockProofChain(
    val from: BlockIdExt,
    val to: BlockIdExt,
    val mode: Int = 0,
    val isComplete: Boolean = false,
    val hasKeyBlock: Boolean = false,
    val isValid: Boolean = false,
    val keyBlockId: BlockIdExt? = null,
    val links: List<BlockProofLink> = emptyList(),
) {
    fun lastLink(): BlockProofLink? = links.lastOrNull()

    fun isLastLinkIncomplete(): Boolean = lastLink()?.isIncomplete() ?: false

    suspend fun validate() = coroutineScope {
        check(from.isMasterchainExt() && to.isMasterchainExt()) {
            "BlockProofChain must have both source and destination blocks in the masterchain"
        }
        if (links.isEmpty()) {
            check(from == to) {
                "BlockProofChain has no links, but its source block $from and destination block $to are different"
            }
            return@coroutineScope
        }
        var current = from
        links.forEachIndexed { index, link ->
            check(link.from == current) {
                "BlockProofChain link #$index has source block ${link.from}, but previous link has destination block $current"
            }
            coroutineContext.ensureActive()
            link.validate()
        }
    }
}
