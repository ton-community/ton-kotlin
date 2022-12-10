@file:Suppress("NOTHING_TO_INLINE")

package org.ton.api.adnl

import io.ktor.util.*
import io.ktor.utils.io.core.*
import org.ton.api.adnl.AdnlIdShort.Companion.SIZE_BYTES
import org.ton.api.dht.DhtNode
import org.ton.api.overlay.OverlayNode
import org.ton.api.overlay.OverlayNodeToSign
import org.ton.bitstring.BitString
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.TlObject
import org.ton.tl.constructors.readInt256Tl
import org.ton.tl.constructors.writeInt256Tl
import kotlin.jvm.JvmStatic

inline fun AdnlIdShort(byteArray: ByteArray): AdnlIdShort = AdnlIdShort.of(byteArray)
inline fun AdnlIdShort(input: Input): AdnlIdShort = AdnlIdShort.of(input)
inline fun AdnlIdShort(bitString: BitString): AdnlIdShort = AdnlIdShort.of(bitString)

interface AdnlIdShort : Comparable<AdnlIdShort>, TlObject<AdnlIdShort> {
    val id: ByteArray

    fun toBitString(): BitString

    fun verify(node: OverlayNode): Boolean

    companion object : TlCodec<AdnlIdShort> by AdnlIdShortTlConstructor {
        const val SIZE_BYTES = 32

        @JvmStatic
        fun tlConstructor(): TlConstructor<AdnlIdShort> = AdnlIdShortTlConstructor

        @JvmStatic
        fun of(byteArray: ByteArray): AdnlIdShort = AdnlIdShortImpl(byteArray.copyOf(SIZE_BYTES))

        @JvmStatic
        fun of(input: Input): AdnlIdShort = AdnlIdShortImpl(input.readBytes(SIZE_BYTES))

        @JvmStatic
        fun of(bitString: BitString): AdnlIdShort = AdnlIdShortImpl(bitString.toByteArray())
    }
}

private data class AdnlIdShortImpl(
    private val _id: ByteArray
) : AdnlIdShort {
    private val _hashCode by lazy {
        _id.contentHashCode()
    }
    private val _string by lazy {
        _id.encodeBase64()
    }

    override val id: ByteArray get() = _id.copyOf()

    override fun toBitString(): BitString = BitString(_id)

    override fun tlCodec() = AdnlIdShort.tlConstructor()

    override fun verify(node: OverlayNode): Boolean {
        if (!node.overlay.contentEquals(_id)) return false
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
        val otherId = if (other is AdnlIdShortImpl) other._id else other.id
        repeat(32) { i ->
            val result = _id[i].toUByte().compareTo(otherId[i].toUByte())
            if (result != 0) {
                return result
            }
        }
        return _id.size.compareTo(otherId.size)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherId = when (other) {
            is AdnlIdShortImpl -> other._id
            is AdnlIdShort -> other.id
            else -> return false
        }
        if (!_id.contentEquals(otherId)) return false
        return true
    }

    override fun hashCode(): Int = _hashCode

    override fun toString(): String = _string
}

private object AdnlIdShortTlConstructor : TlConstructor<AdnlIdShort>(
    schema = "adnl.id.short id:int256 = adnl.id.Short"
) {
    override fun decode(input: Input): AdnlIdShort {
        val id = input.readInt256Tl()
        return AdnlIdShortImpl(id)
    }

    override fun encode(output: Output, value: AdnlIdShort) {
        output.writeInt256Tl(value.id)
    }
}
