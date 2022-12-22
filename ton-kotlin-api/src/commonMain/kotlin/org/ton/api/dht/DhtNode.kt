package org.ton.api.dht

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.api.SignedTlObject
import org.ton.api.adnl.AdnlAddressList
import org.ton.api.adnl.AdnlIdShort
import org.ton.api.adnl.AdnlNode
import org.ton.api.pk.PrivateKey
import org.ton.api.pub.PublicKey
import org.ton.crypto.Base64ByteArraySerializer
import org.ton.tl.*

@Serializable
@JsonClassDiscriminator("@type")
public data class DhtNode(
    val id: PublicKey,
    @SerialName("addr_list")
    val addrList: AdnlAddressList,
    val version: Int = 0,
    @Serializable(Base64ByteArraySerializer::class)
    override val signature: ByteArray = ByteArray(0)
) : SignedTlObject<DhtNode> {
    public fun toAdnlNode(): AdnlNode = AdnlNode(id, addrList)
    public fun key(): AdnlIdShort = id.toAdnlIdShort()

    override fun signed(privateKey: PrivateKey): DhtNode =
        copy(signature = privateKey.sign(tlCodec().encodeToByteArray(this)))

    override fun verify(publicKey: PublicKey): Boolean =
        publicKey.verify(tlCodec().encodeToByteArray(copy(signature = ByteArray(0))), signature)

    override fun tlCodec(): TlCodec<DhtNode> = DhtNodeTlConstructor

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DhtNode) return false
        if (id != other.id) return false
        if (addrList != other.addrList) return false
        if (version != other.version) return false
        if (!signature.contentEquals(other.signature)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + addrList.hashCode()
        result = 31 * result + version
        result = 31 * result + signature.contentHashCode()
        return result
    }

    public companion object : TlCodec<DhtNode> by DhtNodeTlConstructor
}

private object DhtNodeTlConstructor : TlConstructor<DhtNode>(
    schema = "dht.node id:PublicKey addr_list:adnl.addressList version:int signature:bytes = dht.Node"
) {
    override fun encode(writer: TlWriter, value: DhtNode) {
        writer.write(PublicKey, value.id)
        writer.write(AdnlAddressList, value.addrList)
        writer.writeInt(value.version)
        writer.writeBytes(value.signature)
    }

    override fun decode(reader: TlReader): DhtNode {
        val id = reader.read(PublicKey)
        val addrList = reader.read(AdnlAddressList)
        val version = reader.readInt()
        val signature = reader.readBytes()
        return DhtNode(id, addrList, version, signature)
    }
}
