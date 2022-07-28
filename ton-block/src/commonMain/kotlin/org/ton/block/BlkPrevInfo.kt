@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor

@JsonClassDiscriminator("@type")
@Serializable
sealed interface BlkPrevInfo {
    companion object {
        @JvmStatic
        fun tlbCodec(multiple: Boolean): TlbCombinator<BlkPrevInfo> =
            BlkPrevInfoTlbCombinator(multiple)

        @JvmStatic
        fun tlbCodec(multiple: Number): TlbCombinator<BlkPrevInfo> =
            BlkPrevInfoTlbCombinator(multiple != 0)
    }

    fun prevs(): List<ExtBlkRef>
}

private class BlkPrevInfoTlbCombinator(
    val multiple: Boolean
) : TlbCombinator<BlkPrevInfo>() {
    val prevBlkInfo by lazy { PrevBlkInfo.tlbCodec() }
    val prevBlksInfo by lazy { PrevBlksInfo.tlbCodec() }

    override val constructors: List<TlbConstructor<out BlkPrevInfo>> by lazy {
        listOf(prevBlkInfo, prevBlksInfo)
    }

    override fun getConstructor(
        value: BlkPrevInfo
    ): TlbConstructor<out BlkPrevInfo> = when (value) {
        is PrevBlkInfo -> prevBlkInfo
        is PrevBlksInfo -> prevBlksInfo
    }

    override fun loadTlb(cellSlice: CellSlice): BlkPrevInfo {
        return if (multiple) {
            prevBlksInfo.loadTlb(cellSlice)
        } else {
            prevBlkInfo.loadTlb(cellSlice)
        }
    }
}
