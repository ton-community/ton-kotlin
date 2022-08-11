package org.ton.api.adnl

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.pub.PublicKey
import org.ton.tl.TlConstructor
import org.ton.tl.readTl
import org.ton.tl.writeTl

@Serializable
data class AdnlNode(
    val id: PublicKey,
    @SerialName("addr_list")
    val addr_list: AdnlAddressList
) {
    companion object : TlConstructor<AdnlNode>(
        type = AdnlNode::class,
        schema = "adnl.node id:PublicKey addr_list:adnl.addressList = adnl.Node"
    ) {
        override fun encode(output: Output, value: AdnlNode) {
            output.writeTl(PublicKey, value.id)
            output.writeTl(AdnlAddressList, value.addr_list)
        }

        override fun decode(input: Input): AdnlNode {
            val id = input.readTl(PublicKey)
            val addrList = input.readTl(AdnlAddressList)
            return AdnlNode(id, addrList)
        }
    }
}
