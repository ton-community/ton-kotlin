package org.ton.api.adnl.proxy

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.crypto.Base64ByteArraySerializer
import org.ton.crypto.base64
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readBytesTl
import org.ton.tl.constructors.readInt256Tl
import org.ton.tl.constructors.writeBytesTl
import org.ton.tl.constructors.writeInt256Tl

@SerialName("adnl.proxy.fast")
@Serializable
data class AdnlProxyFast(
    @Serializable(Base64ByteArraySerializer::class)
    override val id: ByteArray,
    @Serializable(Base64ByteArraySerializer::class)
    val shared_secret: ByteArray
) : AdnlProxy {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AdnlProxyFast

        if (!id.contentEquals(other.id)) return false
        if (!shared_secret.contentEquals(other.shared_secret)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.contentHashCode()
        result = 31 * result + shared_secret.contentHashCode()
        return result
    }

    override fun toString(): String = buildString {
        append("AdnlProxyFast(id=")
        append(base64(id))
        append(", sharedSecret=")
        append(base64(shared_secret))
        append(")")
    }

    companion object : TlConstructor<AdnlProxyFast>(
        type = AdnlProxyFast::class,
        schema = "adnl.proxy.fast id:int256 shared_secret:bytes = adnl.Proxy"
    ) {
        override fun encode(output: Output, value: AdnlProxyFast) {
            output.writeInt256Tl(value.id)
            output.writeBytesTl(value.shared_secret)
        }

        override fun decode(input: Input): AdnlProxyFast {
            val id = input.readInt256Tl()
            val sharedSecret = input.readBytesTl()
            return AdnlProxyFast(id, sharedSecret)
        }
    }
}
