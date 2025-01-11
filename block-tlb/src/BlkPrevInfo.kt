@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.Serializable
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbObject
import kotlin.jvm.JvmStatic


@Serializable
public sealed interface BlkPrevInfo : TlbObject {
    public companion object {
        @Suppress("UNCHECKED_CAST")
        @JvmStatic
        public fun tlbCodec(multiple: Boolean): TlbCodec<BlkPrevInfo> =
            (if (multiple) PrevBlksInfo else PrevBlkInfo) as TlbCodec<BlkPrevInfo>

        @JvmStatic
        public fun tlbCodec(multiple: Int): TlbCodec<BlkPrevInfo> =
            tlbCodec(multiple != 0)
    }
}
