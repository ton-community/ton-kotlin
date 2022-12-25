@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tlb.TlbCodec
import kotlin.jvm.JvmStatic

@JsonClassDiscriminator("@type")
@Serializable
sealed interface BlkPrevInfo {
    companion object {
        @Suppress("UNCHECKED_CAST")
        @JvmStatic
        fun tlbCodec(multiple: Boolean): TlbCodec<BlkPrevInfo> =
            (if (multiple) PrevBlksInfo.tlbCodec() else PrevBlkInfo.tlbCodec()) as TlbCodec<BlkPrevInfo>

        @JvmStatic
        fun tlbCodec(multiple: Int): TlbCodec<BlkPrevInfo> =
            tlbCodec(multiple != 0)
    }

    fun prevs(): List<ExtBlkRef>
}
