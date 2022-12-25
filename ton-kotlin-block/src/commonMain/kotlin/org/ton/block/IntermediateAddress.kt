@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tlb.TlbCombinator
import org.ton.tlb.providers.TlbCombinatorProvider

@Serializable
@JsonClassDiscriminator("@type")
sealed interface IntermediateAddress {
    companion object : TlbCombinatorProvider<IntermediateAddress> by IntermediateAddressTlbCombinator
}

private object IntermediateAddressTlbCombinator : TlbCombinator<IntermediateAddress>(
    IntermediateAddress::class,
    IntermediateAddressExt::class to IntermediateAddressExt,
    IntermediateAddressRegular::class to IntermediateAddressRegular,
    IntermediateAddressSimple::class to IntermediateAddressSimple,
)
