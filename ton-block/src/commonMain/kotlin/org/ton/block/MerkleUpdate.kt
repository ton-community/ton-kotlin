package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString

@SerialName("merkle_update")
@Serializable
data class MerkleUpdate<X : Any>(
    val old_hash: BitString,
    val new_hash: BitString,
    val old: X,
    val new: X
) {
    init {
        require(old_hash.size == 256) { "required: old_hash.size = 256, actual: ${old_hash.size}" }
        require(new_hash.size == 256) { "required: new_hash.size = 256, actual: ${new_hash.size}" }
    }
}
