@file:Suppress("NOTHING_TO_INLINE")

package org.ton.api.adnl

import io.ktor.util.*
import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.api.overlay.OverlayNode
import org.ton.api.overlay.OverlayNodeToSign
import org.ton.bitstring.BitString
import org.ton.tl.*
import kotlin.jvm.JvmStatic

public inline fun AdnlIdShort(byteArray: ByteArray): AdnlIdShort = AdnlIdShort.of(byteArray)
public inline fun AdnlIdShort(bitString: BitString): AdnlIdShort = AdnlIdShort.of(bitString)
public inline fun AdnlIdShort(bitString: Bits256): AdnlIdShort = AdnlIdShort.of(bitString)

public interface AdnlIdShort : Comparable<AdnlIdShort>, TlObject<AdnlIdShort> {
    public val id: Bits256

    public fun verify(node: OverlayNode): Boolean

    public companion object : TlCodec<AdnlIdShort> by AdnlIdShortTlConstructor {
        public const val SIZE_BYTES: Int = 32

        @JvmStatic
        public fun tlConstructor(): TlConstructor<AdnlIdShort> = AdnlIdShortTlConstructor

        @JvmStatic
        public fun of(byteArray: ByteArray): AdnlIdShort = of(Bits256(byteArray))

        @JvmStatic
        public fun of(bitString: BitString): AdnlIdShort = of(Bits256(bitString))

        @JvmStatic
        public fun of(bitString: Bits256): AdnlIdShort = AdnlIdShortImpl(bitString)
    }
}

@Serializable
private data class AdnlIdShortImpl(
    override val id: Bits256
) : AdnlIdShort {
    private val _hashCode by lazy(LazyThreadSafetyMode.PUBLICATION) {
        id.hashCode()
    }
    private val _string by lazy(LazyThreadSafetyMode.PUBLICATION) {
        id.toString()
    }

    override fun tlCodec() = AdnlIdShort.tlConstructor()

    override fun verify(node: OverlayNode): Boolean {
        if (node.overlay != (id)) return false
        val key = node.id
        val peerId = key.toAdnlIdShort()
        val nodeToSign = OverlayNodeToSign(
            id = peerId,
            overlay = node.overlay,
            version = node.version
        )
        return key.verify(nodeToSign.toByteArray(), node.signature)
    }

    override fun compareTo(other: AdnlIdShort): Int = id.compareTo(other.id)

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
        val id = reader.readBits256()
        return AdnlIdShortImpl(id)
    }

    override fun encode(writer: TlWriter, value: AdnlIdShort) {
        writer.writeBits256(value.id)
    }
}
