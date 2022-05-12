package org.ton.api.adnl

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.pub.PublicKey
import org.ton.tl.TlConstructor

@Serializable
data class AdnlNode(
        val id: PublicKey,
        @SerialName("addr_list")
        val addrList: AdnlAddressList
) {
    companion object : TlConstructor<AdnlNode>(
            type = AdnlNode::class,
            schema = "adnl.node id:PublicKey addr_list:adnl.addressList = adnl.Node"
    ) {
        override fun encode(output: Output, message: AdnlNode) {
            output.writeTl(message.id, PublicKey)
            output.writeTl(message.addrList, AdnlAddressList)
        }

        override fun decode(input: Input): AdnlNode {
            val id = input.readTl(PublicKey)
            val addrList = input.readTl(AdnlAddressList)
            return AdnlNode(id, addrList)
        }
    }
}