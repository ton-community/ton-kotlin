package org.ton.api.adnl

import io.ktor.utils.io.core.*
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readIntTl
import org.ton.tl.constructors.readVectorTl
import org.ton.tl.constructors.writeIntTl
import org.ton.tl.constructors.writeVectorTl

@SerialName("adnl.addressList")
@Polymorphic
@Serializable
@JsonClassDiscriminator("@type")
data class AdnlAddressList(
    val addrs: List<AdnlAddress> = emptyList(),
    val version: Int = 0,
    val reinit_date: Int = 0,
    val priority: Int = 0,
    val expire_at: Int = 0
) : List<AdnlAddress> by addrs {
    companion object : TlConstructor<AdnlAddressList>(
        type = AdnlAddressList::class,
        schema = "adnl.addressList addrs:(vector adnl.Address) version:int reinit_date:int priority:int expire_at:int = adnl.AddressList"
    ) {
        override fun encode(output: Output, value: AdnlAddressList) {
            output.writeVectorTl(value.addrs, AdnlAddress)
            output.writeIntTl(value.version)
            output.writeIntTl(value.reinit_date)
            output.writeIntTl(value.priority)
            output.writeIntTl(value.expire_at)
        }

        override fun decode(input: Input): AdnlAddressList {
            val addrs = input.readVectorTl(AdnlAddress)
            val version = input.readIntTl()
            val reinitDate = input.readIntTl()
            val priority = input.readIntTl()
            val expireAt = input.readIntTl()
            return AdnlAddressList(addrs, version, reinitDate, priority, expireAt)
        }
    }
}