package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString

@Serializable
@SerialName("sig_pair")
data class CryptoSignaturePair(
    val node_id_short: BitString,
    val sign: CryptoSignature
)