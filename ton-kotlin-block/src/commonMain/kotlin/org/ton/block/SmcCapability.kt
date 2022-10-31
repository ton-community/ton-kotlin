package org.ton.block

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.*
import org.ton.tlb.providers.TlbCombinatorProvider

sealed interface SmcCapability {
    object MethodSeqno : SmcCapability
    object MethodPubKey : SmcCapability
    object IsWallet : SmcCapability
    data class Name(
        val name: Text
    ) : SmcCapability

    companion object : TlbCombinatorProvider<SmcCapability> by SmcCapabilityTlbConstructor
}

private object SmcCapabilityTlbConstructor : TlbCombinator<SmcCapability>() {
    val methodSeqno = ObjectTlbConstructor(
        schema = "cap_method_seqno#5371 = SmcCapability;",
        SmcCapability.MethodSeqno
    )
    val methodPubKey = ObjectTlbConstructor(
        schema = "cap_method_pubkey#71f4 = SmcCapability;",
        SmcCapability.MethodPubKey
    )
    val isWallet = ObjectTlbConstructor(
        schema = "cap_is_wallet#2177 = SmcCapability;",
        SmcCapability.IsWallet
    )
    val capName = object : TlbConstructor<SmcCapability.Name>(
        schema = "cap_name#ff name:Text = SmcCapability;"
    ) {
        override fun loadTlb(cellSlice: CellSlice): SmcCapability.Name {
            val name = cellSlice.loadTlb(Text)
            return SmcCapability.Name(name)
        }
        override fun storeTlb(cellBuilder: CellBuilder, value: SmcCapability.Name) {
            cellBuilder.storeTlb(Text, value.name)
        }
    }

    override val constructors: List<TlbConstructor<out SmcCapability>> = listOf(
        methodSeqno,
        methodPubKey,
        isWallet,
        capName
    )
}
