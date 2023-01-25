package org.ton.tonlib.block

import org.ton.lite.api.liteserver.LiteServerSignature
import org.ton.tonlib.NodeIdShort

internal data class BlockSignature(
    val node: NodeIdShort,
    val signature: ByteArray
) {
    constructor(tl: LiteServerSignature) : this(
        tl.nodeIdShort,
        tl.signature
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BlockSignature) return false
        if (node != other.node) return false
        if (!signature.contentEquals(other.signature)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = node.hashCode()
        result = 31 * result + signature.contentHashCode()
        return result
    }
}
