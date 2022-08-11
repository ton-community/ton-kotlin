@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor

@Serializable
@JsonClassDiscriminator("@type")
sealed interface CryptoSignature {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbCombinator<CryptoSignature> = CryptoSignatureTlbCombinator
    }
}

private object CryptoSignatureTlbCombinator : TlbCombinator<CryptoSignature>() {
    val regular by lazy { CryptoSignatureSimple.tlbCodec() }
    val chained by lazy { ChainedSignature.tlbCodec() }

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


