@file:Suppress("OPT_IN_USAGE")

package org.ton.api.adnl

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.crypto.Base64ByteArraySerializer
import org.ton.tl.*

@JsonClassDiscriminator("@type")
public interface AdnlProxy {
    public val id: Bits256

    public companion object : TlCombinator<AdnlProxy>(
        AdnlProxy::class,
        AdnlProxyNone::class to AdnlProxyNone,
        AdnlProxyFast::class to AdnlProxyFast
    )
}

@SerialName("adnl.proxy.none")
@Serializable
public data class AdnlProxyNone(
    override val id: Bits256
) : AdnlProxy {
    public companion object : TlConstructor<AdnlProxyNone>(
        schema = "adnl.proxy.none id:int256 = adnl.Proxy"
    ) {
        override fun encode(writer: TlWriter, value: AdnlProxyNone) {
            writer.writeBits256(value.id)
        }

        override fun decode(reader: TlReader): AdnlProxyNone {
            val id = reader.readBits256()
            return AdnlProxyNone(id)
        }
    }
}

@SerialName("adnl.proxy.fast")
@Serializable
public data class AdnlProxyFast(
    override val id: Bits256,
    @SerialName("shared_secret")
    @Serializable(Base64ByteArraySerializer::class)
    val sharedSecret: ByteArray
) : AdnlProxy {
    public companion object : TlConstructor<AdnlProxyFast>(
        schema = "adnl.proxy.fast id:int256 shared_secret:bytes = adnl.Proxy"
    ) {
        override fun encode(writer: TlWriter, value: AdnlProxyFast) {
            writer.writeBits256(value.id)
            writer.writeBytes(value.sharedSecret)
        }

        override fun decode(reader: TlReader): AdnlProxyFast {
            val id = reader.readBits256()
            val sharedSecret = reader.readBytes()
            return AdnlProxyFast(id, sharedSecret)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AdnlProxyFast) return false

        if (id != other.id) return false
        if (!sharedSecret.contentEquals(other.sharedSecret)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + sharedSecret.contentHashCode()
        return result
    }
}
