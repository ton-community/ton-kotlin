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

private object CryptoSignatureTlbCombinator : TlbCombinator<CryptoSignature>() {
    val regular = CryptoSignatureSimple.tlbConstructor()
    val chained = ChainedSignature.tlbConstructor()

    override val constructors: List<TlbConstructor<out CryptoSignature>> by lazy {
        listOf(regular, chained)
    }

    override fun getConstructor(
        value: CryptoSignature
    ): TlbConstructor<out CryptoSignature> = when (value) {
        is CryptoSignatureSimple -> regular
        is ChainedSignature -> chained
    }
}


