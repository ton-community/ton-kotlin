@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor

@JsonClassDiscriminator("@type")
@Serializable
sealed interface CommonMsgInfo {
    companion object : TlbCodec<CommonMsgInfo> by CommonMsgInfoTlbCombinator {
        @JvmStatic
        fun tlbCodec(): TlbCombinator<CommonMsgInfo> = CommonMsgInfoTlbCombinator
    }
}

private object CommonMsgInfoTlbCombinator : TlbCombinator<CommonMsgInfo>() {
    private val intMsgInfoConstructor = IntMsgInfo.tlbCodec()
    private val extInMsgConstructor = ExtInMsgInfo.tlbCodec()
    private val extOutMsgInfoConstructor = ExtOutMsgInfo.tlbCodec()

    override val constructors: List<TlbConstructor<out CommonMsgInfo>> =
        listOf(intMsgInfoConstructor, extInMsgConstructor, extOutMsgInfoConstructor)

    override fun getConstructor(value: CommonMsgInfo): TlbConstructor<out CommonMsgInfo> = when (value) {
        is IntMsgInfo -> intMsgInfoConstructor
        is ExtInMsgInfo -> extInMsgConstructor
        is ExtOutMsgInfo -> extOutMsgInfoConstructor
    }
}
