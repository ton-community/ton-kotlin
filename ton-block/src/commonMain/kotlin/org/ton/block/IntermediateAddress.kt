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
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbCodec<IntermediateAddress> = IntermediateAddressTlbCombinator
    }
}

private object IntermediateAddressTlbCombinator : TlbCombinator<IntermediateAddress>() {
    val regular by lazy {
        IntermediateAddressRegular.tlbCodec()
    }
    val simple by lazy {
        IntermediateAddressSimple.tlbCodec()
    }
    val ext by lazy {
        IntermediateAddressExt.tlbCodec()
    }
    override val constructors: List<TlbConstructor<out IntermediateAddress>> by lazy {
        listOf(regular, simple, ext)
    }

    override fun getConstructor(
        value: IntermediateAddress
    ): TlbConstructor<out IntermediateAddress> = when (value) {
        is IntermediateAddressRegular -> regular
        is IntermediateAddressSimple -> simple
        is IntermediateAddressExt -> ext
    }
}


