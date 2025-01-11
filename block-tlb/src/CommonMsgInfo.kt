@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.Serializable

import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbObject
import kotlin.jvm.JvmStatic


@Serializable
public sealed interface CommonMsgInfo : TlbObject {
    public companion object : TlbCodec<CommonMsgInfo> by CommonMsgInfoTlbCombinator {
        @JvmStatic
        public fun tlbCodec(): TlbCombinator<CommonMsgInfo> = CommonMsgInfoTlbCombinator
    }
}

private object CommonMsgInfoTlbCombinator : TlbCombinator<CommonMsgInfo>(
    CommonMsgInfo::class,
    IntMsgInfo::class to IntMsgInfo,
    ExtInMsgInfo::class to ExtInMsgInfo,
    ExtOutMsgInfo::class to ExtOutMsgInfo
)
