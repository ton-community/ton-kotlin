package org.ton.api.adnl

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tl.*

@SerialName("adnl.addressList")
@Serializable
@JsonClassDiscriminator("@type")
public data class AdnlAddressList(
    val addrs: Collection<AdnlAddress> = emptyList(),
    val version: Int = 0,
    @SerialName("reinit_date")
    val reinitDate: Int = 0,
    val priority: Int = 0,
    @SerialName("expire_at")
    val expireAt: Int = 0
) : TlObject<AdnlAddressList>, Collection<AdnlAddress> by addrs {
    public constructor(vararg addrs: AdnlAddress) : this(addrs.toList())

    override fun tlCodec(): TlCodec<AdnlAddressList> = Companion

    public companion object : TlConstructor<AdnlAddressList>(
        schema = "adnl.addressList addrs:(vector adnl.Address) version:int reinit_date:int priority:int expire_at:int = adnl.AddressList"
    ) {
        override fun encode(writer: TlWriter, value: AdnlAddressList) {
            writer.writeVector(value.addrs) {
                write(AdnlAddress, it)
            }
            writer.writeInt(value.version)
            writer.writeInt(value.reinitDate)
            writer.writeInt(value.priority)
            writer.writeInt(value.expireAt)
        }

        override fun decode(reader: TlReader): AdnlAddressList {
            val addrs = reader.readVector {
                read(AdnlAddress)
            }
            val version = reader.readInt()
            val reinitDate = reader.readInt()
            val priority = reader.readInt()
            val expireAt = reader.readInt()
            return AdnlAddressList(addrs, version, reinitDate, priority, expireAt)
        }
    }
}
