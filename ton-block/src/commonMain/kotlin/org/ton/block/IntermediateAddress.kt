@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor

@Serializable
@JsonClassDiscriminator("@type")
sealed interface IntermediateAddress {
    companion object : TlbCodec<IntermediateAddress> by IntermediateAddressTlbCombinator {
        @JvmStatic
        fun tlbCodec(): TlbCodec<IntermediateAddress> = IntermediateAddressTlbCombinator
    }
}

private object IntermediateAddressTlbCombinator : TlbCombinator<IntermediateAddress>() {
    val regular = IntermediateAddressRegular.tlbCodec()
    val simple = IntermediateAddressSimple.tlbCodec()
    val ext = IntermediateAddressExt.tlbCodec()

    override val constructors: List<TlbConstructor<out IntermediateAddress>> = listOf(
        regular, simple, ext
    )

    override fun getConstructor(
        value: IntermediateAddress
    ): TlbConstructor<out IntermediateAddress> = when (value) {
        is IntermediateAddressRegular -> regular
        is IntermediateAddressSimple -> simple
        is IntermediateAddressExt -> ext
    }
}


