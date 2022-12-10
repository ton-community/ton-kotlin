@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbCombinatorProvider

@Serializable
@JsonClassDiscriminator("@type")
sealed interface CryptoSignature {
    companion object : TlbCombinatorProvider<CryptoSignature> by CryptoSignatureTlbCombinator
}

private object CryptoSignatureTlbCombinator : TlbCombinator<CryptoSignature>(
    CryptoSignature::class,
    CryptoSignatureSimple::class to CryptoSignatureSimple,
    ChainedSignature::class to ChainedSignature
)
