package org.ton.api.dht

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.adnl.AdnlAddressList
import org.ton.api.pub.PublicKey
import org.ton.crypto.Base64ByteArraySerializer
import org.ton.crypto.base64
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readBytesTl
import org.ton.tl.constructors.readIntTl
import org.ton.tl.constructors.writeBytesTl
import org.ton.tl.constructors.writeIntTl
import org.ton.tl.readTl
import org.ton.tl.writeTl

@Serializable
data class DhtNode(
        val id: PublicKey,
        @SerialName("addr_list")
        val addrList: AdnlAddressList,
        val version: Int,
        @Serializable(Base64ByteArraySerializer::class)
        val signature: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DhtNode

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

    override fun toString(): String = buildString {
        append("DhtNode(id=")
        append(id)
        append(", addrList=")
        append(addrList)
        append(", version=")
        append(version)
        append(", signature=")
        append(base64(signature))
        append(")")
    }

    companion object : TlConstructor<DhtNode>(
            type = DhtNode::class,
            schema = "dht.node id:PublicKey addr_list:adnl.addressList version:int signature:bytes = dht.Node"
    ) {
        override fun encode(output: Output, value: DhtNode) {
            output.writeTl(value.id, PublicKey)
            output.writeTl(value.addrList, AdnlAddressList)
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
}