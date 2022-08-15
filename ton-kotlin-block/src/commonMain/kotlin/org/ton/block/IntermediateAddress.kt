@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbCombinatorProvider

@Serializable
@JsonClassDiscriminator("@type")
sealed interface IntermediateAddress {
    companion object : TlbCombinatorProvider<IntermediateAddress> by IntermediateAddressTlbCombinator
}

private object IntermediateAddressTlbCombinator : TlbCombinator<IntermediateAddress>() {
    val regular = IntermediateAddressRegular.tlbConstructor()
    val simple = IntermediateAddressSimple.tlbConstructor()
    val ext = IntermediateAddressExt.tlbConstructor()

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


