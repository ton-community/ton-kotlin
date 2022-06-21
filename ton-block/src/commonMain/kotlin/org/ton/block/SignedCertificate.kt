package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@Serializable
@SerialName("signed_certificate")
data class SignedCertificate(
    val certificate: Certificate,
    val certificate_signature: CryptoSignature
) {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<SignedCertificate> = SignedCertificatedTlbConstructor
    }
}

private object SignedCertificatedTlbConstructor : TlbConstructor<SignedCertificate>(
    schema = "signed_certificate\$_ certificate:Certificate certificate_signature:CryptoSignature\n" +
            "  = SignedCertificate;"
) {
    val certificate by lazy { Certificate.tlbCodec() }
    val cryptoSignature by lazy { CryptoSignature.tlbCodec() }

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: SignedCertificate
    ) = cellBuilder {
        storeTlb(certificate, value.certificate)
        storeTlb(cryptoSignature, value.certificate_signature)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): SignedCertificate = cellSlice {
        val certificate = loadTlb(certificate)
        val certificateSignature = loadTlb(cryptoSignature)
        SignedCertificate(certificate, certificateSignature)
    }
}