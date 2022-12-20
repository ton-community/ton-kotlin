@file:Suppress("OPT_IN_USAGE")

package org.ton.api.adnl

import io.ktor.utils.io.core.*
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.api.pub.PublicKey
import org.ton.bitstring.BitString
import org.ton.tl.*

@Polymorphic
@Serializable
@JsonClassDiscriminator("@type")
public sealed interface AdnlAddress : TlObject<AdnlAddress> {
    override fun tlCodec(): TlCodec<out AdnlAddress> = Companion

    public companion object : TlCombinator<AdnlAddress>(
        AdnlAddress::class,
        AdnlAddressUdp::class to AdnlAddressUdp,
        AdnlAddressUdp6::class to AdnlAddressUdp6,
        AdnlAddressTunnel::class to AdnlAddressTunnel,
    )
}

public sealed interface AdnlIp : AdnlAddress {
    public val ip: Int
    public val port: Int
}

public sealed interface AdnlIp6 : AdnlAddress {
    public val ip: Bits128
    public val port: Int
}

@SerialName("adnl.address.udp")
@Serializable
public data class AdnlAddressUdp(
    override val ip: Int,
    override val port: Int
) : AdnlAddress, AdnlIp {
    public companion object : TlConstructor<AdnlAddressUdp>(
        schema = "adnl.address.udp ip:int port:int = adnl.Address"
    ) {
        override fun encode(writer: TlWriter, value: AdnlAddressUdp) {
            writer.writeInt(value.ip)
            writer.writeInt(value.port)
        }

        override fun decode(reader: TlReader): AdnlAddressUdp {
            val ip = reader.readInt()
            val port = reader.readInt()
            return AdnlAddressUdp(ip, port)
        }
    }

    override fun toString(): String = Json.encodeToString(this)
}

@JsonClassDiscriminator("@type")
@SerialName("adnl.address.udp6")
@Serializable
public data class AdnlAddressUdp6(
    override val ip: Bits128,
    override val port: Int
) : AdnlAddress, AdnlIp6 {
    public constructor(ip: ByteArray, port: Int) : this(Bits128(ip), port)

    public companion object : TlConstructor<AdnlAddressUdp6>(
        schema = "adnl.address.udp6 ip:int128 port:int = adnl.Address"
    ) {
        override fun decode(reader: TlReader): AdnlAddressUdp6 {
            val ip = reader.readBits128()
            val port = reader.readInt()
            return AdnlAddressUdp6(ip, port)
        }

        override fun encode(writer: TlWriter, value: AdnlAddressUdp6) {
            writer.writeBits128(value.ip)
            writer.writeInt(value.port)
        }
    }
}

@JsonClassDiscriminator("@type")
@SerialName("adnl.address.tunnel")
@Serializable
public data class AdnlAddressTunnel(
    val to: Bits256,
    val pubkey: PublicKey
) : AdnlAddress {
    public constructor(to: ByteArray, pubkey: PublicKey) : this(Bits256(to), pubkey)
    public constructor(adnlIdShort: AdnlIdShort, pubKey: PublicKey) : this(adnlIdShort.id, pubKey)

    public companion object : TlConstructor<AdnlAddressTunnel>(
        schema = "adnl.address.tunnel to:int256 pubkey:PublicKey = adnl.Address"
    ) {
        override fun encode(writer: TlWriter, value: AdnlAddressTunnel) {
            writer.writeBits256(value.to)
            writer.write(PublicKey, value.pubkey)
        }

        override fun decode(reader: TlReader): AdnlAddressTunnel {
            val to = reader.readBits256()
            val pubKey = reader.read(PublicKey)
            return AdnlAddressTunnel(to, pubKey)
        }
    }
}
