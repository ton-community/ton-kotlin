package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString

@Serializable
@SerialName("ed25519_pubkey")
data class SigPubKey(
    val pubkey: BitString
) {
    init {
        require(pubkey.size == 256) { "required: pubkey.size == 256, actual: ${pubkey.size}" }
    }
}
