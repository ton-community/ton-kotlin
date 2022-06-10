package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString

@Serializable
@SerialName("merkle_proof")
data class MerkleProof<X : Any>(
    val virtual_hash: BitString,
    val depth: Int,
    val virtual_root: X
) {
    init {
        require(virtual_hash.size == 256) { "required: virtual_hash.size == 256, actual: ${virtual_hash.size}" }
    }
}