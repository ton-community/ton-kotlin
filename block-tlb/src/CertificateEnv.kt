package org.ton.block

import kotlinx.serialization.SerialName


@SerialName("certificate_env")
public data class CertificateEnv(
    val certificate: Certificate
)
