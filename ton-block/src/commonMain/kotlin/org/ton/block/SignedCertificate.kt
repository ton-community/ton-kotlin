package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("signed_certificate")
data class SignedCertificate(
    val certificate: Certificate,
    val certificate_signature: CryptoSignature
)