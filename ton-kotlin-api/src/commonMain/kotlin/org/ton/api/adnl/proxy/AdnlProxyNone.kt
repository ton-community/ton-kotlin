package org.ton.api.adnl.proxy

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.crypto.Base64ByteArraySerializer
import org.ton.crypto.base64
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readInt256Tl
import org.ton.tl.constructors.writeInt256Tl

@SerialName("adnl.proxy.none")
@Serializable
data class AdnlProxyNone(
    @Serializable(Base64ByteArraySerializer::class)
    override val id: ByteArray
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
        override fun encode(output: Output, value: AdnlProxyNone) {
            output.writeInt256Tl(value.id)
        }

        override fun decode(input: Input): AdnlProxyNone {
            val id = input.readInt256Tl()
            return AdnlProxyNone(id)
        }
    }
}
