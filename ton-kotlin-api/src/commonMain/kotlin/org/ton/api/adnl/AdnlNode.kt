package org.ton.api.adnl

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.pub.PublicKey
import org.ton.tl.*

@Serializable
public data class AdnlNode(
    val id: PublicKey,
    @SerialName("addr_list")
    val addr_list: AdnlAddressList
) {
    public constructor(
        id: PublicKey,
        addrList: Collection<AdnlAddress>
    ) : this(id, AdnlAddressList(addrList))

    public companion object : TlConstructor<AdnlNode>(
        schema = "adnl.node id:PublicKey addr_list:adnl.addressList = adnl.Node"
    ) {
        override fun encode(writer: TlWriter, value: AdnlNode) {
            writer.write(PublicKey, value.id)
            writer.write(AdnlAddressList, value.addr_list)
        }

        override fun decode(reader: TlReader): AdnlNode {
            val id = reader.read(PublicKey)
            val addr_list = reader.read(AdnlAddressList)
            return AdnlNode(id, addr_list)
        }
    }
}
