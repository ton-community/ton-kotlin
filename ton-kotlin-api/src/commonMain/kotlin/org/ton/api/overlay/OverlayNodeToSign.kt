package org.ton.api.overlay

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.adnl.AdnlIdShort
import org.ton.tl.*
import org.ton.tl.constructors.readInt256Tl
import org.ton.tl.constructors.readIntTl
import org.ton.tl.constructors.writeInt256Tl
import org.ton.tl.constructors.writeIntTl

@Serializable
@SerialName("overlay.node.toSign")
data class OverlayNodeToSign(
    val id: AdnlIdShort,
    val overlay: ByteArray,
    val version: Int
) : TlObject<OverlayNodeToSign>{
    override fun tlCodec(): TlCodec<OverlayNodeToSign> = Companion

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OverlayNodeToSign) return false

        if (version != other.version) return false
        if (!overlay.contentEquals(other.overlay)) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + overlay.contentHashCode()
        result = 31 * result + version
        return result
    }

    companion object : TlConstructor<OverlayNodeToSign>(
        schema = "overlay.node.toSign id:adnl.id.short overlay:int256 version:int = overlay.node.ToSign",
        type = OverlayNodeToSign::class
    ) {
        override fun encode(output: Output, value: OverlayNodeToSign) {
            output.writeTl(AdnlIdShort, value.id)
            output.writeInt256Tl(value.overlay)
            output.writeIntTl(value.version)
        }

        override fun decode(input: Input): OverlayNodeToSign {
            val id = input.readTl(AdnlIdShort)
            val overlay = input.readInt256Tl()
            val version = input.readIntTl()
            return OverlayNodeToSign(id, overlay, version)
        }
    }
}
