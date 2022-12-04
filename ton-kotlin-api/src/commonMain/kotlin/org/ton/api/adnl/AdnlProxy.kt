@file:Suppress("OPT_IN_USAGE")

package org.ton.api.adnl

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.bitstring.BitString
import org.ton.crypto.base64.Base64ByteArraySerializer
import org.ton.tl.TlCombinator
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readBytesTl
import org.ton.tl.constructors.readInt256Tl
import org.ton.tl.constructors.writeBytesTl
import org.ton.tl.constructors.writeInt256Tl

@JsonClassDiscriminator("@type")
interface AdnlProxy {
    val id: BitString

    companion object : TlCombinator<AdnlProxy>(
        AdnlProxyNone,
        AdnlProxyFast
    )
}

@SerialName("adnl.proxy.none")
@Serializable
data class AdnlProxyNone(
    override val id: BitString
) : AdnlProxy {
    companion object : TlConstructor<AdnlProxyNone>(
        type = AdnlProxyNone::class,
        schema = "adnl.proxy.none id:int256 = adnl.Proxy"
    ) {
        override fun encode(output: Output, value: AdnlProxyNone) {
            output.writeInt256Tl(value.id)
        }

        override fun decode(input: Input): AdnlProxyNone {
            val id = input.readInt256Tl()
            return AdnlProxyNone(BitString(id))
        }
    }
}

@SerialName("adnl.proxy.fast")
@Serializable
data class AdnlProxyFast(
    override val id: BitString,
    @SerialName("shared_secret")
    @Serializable(Base64ByteArraySerializer::class)
    val sharedSecret: ByteArray
) : AdnlProxy {


    companion object : TlConstructor<AdnlProxyFast>(
        type = AdnlProxyFast::class,
        schema = "adnl.proxy.fast id:int256 shared_secret:bytes = adnl.Proxy"
    ) {
        override fun encode(output: Output, value: AdnlProxyFast) {
            output.writeInt256Tl(value.id)
            output.writeBytesTl(value.sharedSecret)
        }

        override fun decode(input: Input): AdnlProxyFast {
            val id = input.readInt256Tl()
            val sharedSecret = input.readBytesTl()
            return AdnlProxyFast(BitString(id), sharedSecret)
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
