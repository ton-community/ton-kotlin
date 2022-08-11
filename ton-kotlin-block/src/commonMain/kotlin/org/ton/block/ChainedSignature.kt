package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.*
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@Serializable
@SerialName("chained_signature")
data class ChainedSignature(
    val signed_crt: SignedCertificate,
    val temp_key_signature: CryptoSignatureSimple
) : CryptoSignature {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<ChainedSignature> = ChainedSignatureTLbConstructor
    }
}

private object ChainedSignatureTLbConstructor : TlbConstructor<ChainedSignature>(
    schema = "chained_signature#f signed_cert:^SignedCertificate temp_key_signature:CryptoSignatureSimple = CryptoSignature;"
) {
    val signedCertificate by lazy { SignedCertificate.tlbCodec() }
    val cryptoSignatureSimple by lazy { CryptoSignatureSimple.tlbCodec() }

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: ChainedSignature
    ) = cellBuilder {
        storeRef {
            storeTlb(signedCertificate, value.signed_crt)
        }
        storeTlb(cryptoSignatureSimple, value.temp_key_signature)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): ChainedSignature = cellSlice {
        val signedCrt = loadRef {
            loadTlb(signedCertificate)
        }
        val tempKetSignature = loadTlb(cryptoSignatureSimple)
        ChainedSignature(signedCrt, tempKetSignature)
    }
}