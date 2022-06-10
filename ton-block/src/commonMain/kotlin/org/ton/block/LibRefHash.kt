package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString

@Serializable
@SerialName("libref_hash")
data class LibRefHash(
    val lib_hash: BitString
) : LibRef {
    init {
        require(lib_hash.size == 256) { "required: lib_hash.size == 256, actual: ${lib_hash.size}" }
    }
}
