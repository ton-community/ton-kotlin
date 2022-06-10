@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.bitstring.BitString

@Serializable
@JsonClassDiscriminator("@type")
sealed interface CryptoSignature

@Serializable
@SerialName("ed25519_signature")
data class CryptoSignatureSimple(
    val r: BitString,
    val s: BitString
) : CryptoSignature

@Serializable
@SerialName("chained_signature")
data class ChainedSignature(
    val signed_crt: SignedCertificate,
    val temp_key_signature: CryptoSignatureSimple
) : CryptoSignature