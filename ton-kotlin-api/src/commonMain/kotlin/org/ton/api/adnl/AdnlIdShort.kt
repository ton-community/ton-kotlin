@file:Suppress("NOTHING_TO_INLINE")

package org.ton.api.adnl

import kotlinx.serialization.Serializable
import org.ton.api.overlay.OverlayNode
import org.ton.api.overlay.OverlayNodeToSign
import org.ton.bitstring.BitString
import org.ton.tl.*
import kotlin.jvm.JvmStatic

public inline fun AdnlIdShort(byteArray: ByteArray): AdnlIdShort = AdnlIdShort.of(byteArray)
public inline fun AdnlIdShort(bitString: BitString): AdnlIdShort = AdnlIdShort.of(bitString)

public interface AdnlIdShort : Comparable<AdnlIdShort>, TlObject<AdnlIdShort> {
    public val id: ByteArray

    public fun verify(node: OverlayNode): Boolean

    public companion object : TlCodec<AdnlIdShort> by AdnlIdShortTlConstructor {
        public const val SIZE_BYTES: Int = 32

        @JvmStatic
        public fun tlConstructor(): TlConstructor<AdnlIdShort> = AdnlIdShortTlConstructor

        @JvmStatic
        public fun of(byteArray: ByteArray): AdnlIdShort = AdnlIdShortImpl(byteArray)

        @JvmStatic
        public fun of(bitString: BitString): AdnlIdShort = AdnlIdShortImpl(bitString.toByteArray())
    }
}

@Serializable
private data class AdnlIdShortImpl(
    override val id: ByteArray
) : AdnlIdShort {
    private val _hashCode by lazy(LazyThreadSafetyMode.PUBLICATION) {
        id.hashCode()
    }
    private val _string by lazy(LazyThreadSafetyMode.PUBLICATION) {
        id.toString()
    }

    override fun tlCodec() = AdnlIdShort.tlConstructor()

    override fun verify(node: OverlayNode): Boolean {
        if (!node.overlay.contentEquals((id))) return false
        val key = node.id
        val peerId = key.toAdnlIdShort()
        val nodeToSign = OverlayNodeToSign(
            id = peerId,
            overlay = node.overlay,
            version = node.version
        )
        return key.verify(nodeToSign.toByteArray(), node.signature)
    }

    override fun compareTo(other: AdnlIdShort): Int {
        for (i in 0 until 32) {
            val a = id[i].toInt() and 0xFF
            val b = other.id[i].toInt() and 0xFF
            if (a != b) {
                return a - b
            }
        }
        return 0
    }

    override fun hashCode(): Int = _hashCode

    override fun toString(): String = _string

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AdnlIdShort) return false
        if (id != other.id) return false
        return true
    }
}

private object AdnlIdShortTlConstructor : TlConstructor<AdnlIdShort>(
    schema = "adnl.id.short id:int256 = adnl.id.Short"
) {
    override fun decode(reader: TlReader): AdnlIdShort {
        val id = reader.readRaw(32)
        return AdnlIdShortImpl(id)
    }

    override fun encode(writer: TlWriter, value: AdnlIdShort) {
        writer.writeRaw(value.id)
    }
}
