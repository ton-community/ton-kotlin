package org.ton.api.dht

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.api.SignedTlObject
import org.ton.api.adnl.AdnlAddressList
import org.ton.api.adnl.AdnlNode
import org.ton.api.pk.PrivateKey
import org.ton.api.pub.PublicKey
import org.ton.crypto.base64.Base64ByteArraySerializer
import org.ton.crypto.base64.base64
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readBytesTl
import org.ton.tl.constructors.readIntTl
import org.ton.tl.constructors.writeBytesTl
import org.ton.tl.constructors.writeIntTl
import org.ton.tl.readTl
import org.ton.tl.writeTl

@Serializable
@JsonClassDiscriminator("@type")
data class DhtNode(
    val id: PublicKey,
    val addr_list: AdnlAddressList,
    val version: Int = 0,
    @Serializable(Base64ByteArraySerializer::class)
    override val signature: ByteArray = ByteArray(0)
) : SignedTlObject<DhtNode> {
    fun toAdnlNode(): AdnlNode = AdnlNode(id, addr_list)
    fun key() = id.toAdnlIdShort().toBitString()

    override fun signed(privateKey: PrivateKey) =
        copy(signature = privateKey.sign(tlCodec().encodeBoxed(this)))

    override fun verify(publicKey: PublicKey): Boolean =
        publicKey.verify(tlCodec().encodeBoxed(copy(signature = ByteArray(0))), signature)

    override fun tlCodec(): TlCodec<DhtNode> = DhtNodeTlConstructor

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DhtNode

        if (id != other.id) return false
        if (addr_list != other.addr_list) return false
        if (version != other.version) return false
        if (!signature.contentEquals(other.signature)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + addr_list.hashCode()
        result = 31 * result + version
        result = 31 * result + signature.contentHashCode()
        return result
    }

    override fun toString(): String = buildString {
        append("DhtNode(id=")
        append(id)
        append(", addrList=")
        append(addr_list)
        append(", version=")
        append(version)
        append(", signature=")
        append(base64(signature))
        append(")")
    }

    companion object : TlCodec<DhtNode> by DhtNodeTlConstructor
}

private object DhtNodeTlConstructor : TlConstructor<DhtNode>(
    schema = "dht.node id:PublicKey addr_list:adnl.addressList version:int signature:bytes = dht.Node"
) {
    override fun encode(output: Output, value: DhtNode) {
        output.writeTl(PublicKey, value.id)
        output.writeTl(AdnlAddressList, value.addr_list)
        output.writeIntTl(value.version)
        output.writeBytesTl(value.signature)
    }

    override fun decode(input: Input): DhtNode {
        val id = input.readTl(PublicKey)
        val addrList = input.readTl(AdnlAddressList)
        val version = input.readIntTl()
        val signature = input.readBytesTl()
        return DhtNode(id, addrList, version, signature)
    }
}
