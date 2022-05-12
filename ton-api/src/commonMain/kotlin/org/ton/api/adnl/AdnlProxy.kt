@file:Suppress("OPT_IN_USAGE")

package org.ton.api.adnl

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.crypto.Base64ByteArraySerializer
import org.ton.crypto.base64
import org.ton.tl.TlCombinator
import org.ton.tl.TlConstructor

@JsonClassDiscriminator("@type")
interface AdnlProxy {
    companion object : TlCombinator<AdnlProxy>(
            AdnlProxyNone,
            AdnlProxyFast
    )
}

@SerialName("adnl.proxy.none")
@Serializable
data class AdnlProxyNone(
        @Serializable(Base64ByteArraySerializer::class)
        val id: ByteArray
) : AdnlProxy {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AdnlProxyNone

        if (!id.contentEquals(other.id)) return false

        return true
    }

    override fun hashCode(): Int {
        return id.contentHashCode()
    }

    override fun toString(): String = buildString {
        append("AdnlProxyNone(id=")
        append(base64(id))
        append(")")
    }

    companion object : TlConstructor<AdnlProxyNone>(
            type = AdnlProxyNone::class,
            schema = "adnl.proxy.none id:int256 = adnl.Proxy"
    ) {
        override fun encode(output: Output, message: AdnlProxyNone) {
            output.writeBits256(message.id)
        }

        override fun decode(input: Input): AdnlProxyNone {
            val id = input.readBits256()
            return AdnlProxyNone(id)
        }
    }
}

@SerialName("adnl.proxy.fast")
@Serializable
data class AdnlProxyFast(
        @Serializable(Base64ByteArraySerializer::class)
        val id: ByteArray,
        @SerialName("shared_secret")
        @Serializable(Base64ByteArraySerializer::class)
        val sharedSecret: ByteArray
) : AdnlProxy {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AdnlProxyFast

        if (!id.contentEquals(other.id)) return false
        if (!sharedSecret.contentEquals(other.sharedSecret)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.contentHashCode()
        result = 31 * result + sharedSecret.contentHashCode()
        return result
    }

    override fun toString(): String = buildString {
        append("AdnlProxyFast(id=")
        append(base64(id))
        append(", sharedSecret=")
        append(base64(sharedSecret))
        append(")")
    }

    companion object : TlConstructor<AdnlProxyFast>(
            type = AdnlProxyFast::class,
            schema = "adnl.proxy.fast id:int256 shared_secret:bytes = adnl.Proxy"
    ) {
        override fun encode(output: Output, message: AdnlProxyFast) {
            output.writeBits256(message.id)
            output.writeByteArray(message.sharedSecret)
        }

        override fun decode(input: Input): AdnlProxyFast {
            val id = input.readBits256()
            val sharedSecret = input.readByteArray()
            return AdnlProxyFast(id, sharedSecret)
        }
    }
}