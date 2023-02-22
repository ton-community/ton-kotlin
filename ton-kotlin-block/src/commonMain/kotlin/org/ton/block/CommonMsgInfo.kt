@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.bitstring.BitString
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbLoader
import org.ton.tlb.TlbObject
import kotlin.jvm.JvmStatic

@JsonClassDiscriminator("@type")
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
) {
    override fun findTlbLoaderOrNull(bitString: BitString): TlbLoader<out CommonMsgInfo>? {
        return if (bitString.size >= 2) {
            if (bitString[0]) { // 1
                if (bitString[1]) { // 11
                    ExtOutMsgInfo.tlbConstructor()
                } else { // 10
                    ExtInMsgInfo.tlbConstructor()
                }
            } else { // 0
                IntMsgInfo.tlbConstructor()
            }
        } else {
            null
        }
    }
}
