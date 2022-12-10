@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbCombinator
import kotlin.jvm.JvmStatic

@JsonClassDiscriminator("@type")
@Serializable
sealed interface CommonMsgInfo {
    companion object : TlbCodec<CommonMsgInfo> by CommonMsgInfoTlbCombinator {
        @JvmStatic
        fun tlbCodec(): TlbCombinator<CommonMsgInfo> = CommonMsgInfoTlbCombinator
    }
}

private object CommonMsgInfoTlbCombinator : TlbCombinator<CommonMsgInfo>(
    CommonMsgInfo::class,
    IntMsgInfo::class to IntMsgInfo.tlbCodec(),
    ExtInMsgInfo::class to ExtInMsgInfo.tlbCodec(),
    ExtOutMsgInfo::class to ExtOutMsgInfo.tlbCodec()
)
