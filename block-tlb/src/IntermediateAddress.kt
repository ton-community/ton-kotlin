@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbObject
import org.ton.tlb.providers.TlbCombinatorProvider


@JsonClassDiscriminator("@type")
public sealed interface IntermediateAddress : TlbObject {
    public companion object : TlbCombinatorProvider<IntermediateAddress> by IntermediateAddressTlbCombinator
}

private object IntermediateAddressTlbCombinator : TlbCombinator<IntermediateAddress>(
    IntermediateAddress::class,
    IntermediateAddressExt::class to IntermediateAddressExt,
    IntermediateAddressRegular::class to IntermediateAddressRegular,
    IntermediateAddressSimple::class to IntermediateAddressSimple,
)
