package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("certificate_env")
data class CertificateEnv(
    val certificate: Certificate
)
