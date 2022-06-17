@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor

@JsonClassDiscriminator("@type")
@Serializable
sealed interface CommonMsgInfo {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbCombinator<CommonMsgInfo> = CommonMsgInfoTlbCombinator()
    }
}

private class CommonMsgInfoTlbCombinator : TlbCombinator<CommonMsgInfo>() {
    private val intMsgInfoConstructor by lazy {
        IntMsgInfo.tlbCodec()
    }
    private val extInMsgConstructor by lazy {
        ExtInMsgInfo.tlbCodec()
    }
    private val extOutMsgInfoConstructor by lazy {
        ExtOutMsgInfo.tlbCodec()
    }

    override val constructors: List<TlbConstructor<out CommonMsgInfo>> by lazy {
        listOf(intMsgInfoConstructor, extInMsgConstructor, extOutMsgInfoConstructor)
    }

    override fun getConstructor(value: CommonMsgInfo): TlbConstructor<out CommonMsgInfo> = when (value) {
        is IntMsgInfo -> intMsgInfoConstructor
        is ExtInMsgInfo -> extInMsgConstructor
        is ExtOutMsgInfo -> extOutMsgInfoConstructor
    }
}
