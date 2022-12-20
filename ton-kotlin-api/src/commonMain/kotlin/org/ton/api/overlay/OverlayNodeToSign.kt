package org.ton.api.overlay

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.adnl.AdnlIdShort
import org.ton.tl.*

@Serializable
@SerialName("overlay.node.toSign")
public data class OverlayNodeToSign(
    val id: AdnlIdShort,
    val overlay: Bits256,
    val version: Int
) : TlObject<OverlayNodeToSign> {
    override fun tlCodec(): TlCodec<OverlayNodeToSign> = Companion

    public companion object : TlConstructor<OverlayNodeToSign>(
        schema = "overlay.node.toSign id:adnl.id.short overlay:int256 version:int = overlay.node.ToSign",
    ) {
        override fun encode(writer: TlWriter, value: OverlayNodeToSign) {
            writer.write(AdnlIdShort, value.id)
            writer.writeBits256(value.overlay)
            writer.writeInt(value.version)
        }

        override fun decode(reader: TlReader): OverlayNodeToSign {
            val id = reader.read(AdnlIdShort)
            val overlay = reader.readBits256()
            val version = reader.readInt()
            return OverlayNodeToSign(id, overlay, version)
        }
    }
}
