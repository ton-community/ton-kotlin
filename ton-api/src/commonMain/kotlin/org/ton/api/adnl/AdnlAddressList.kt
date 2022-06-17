package org.ton.api.adnl

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readIntTl
import org.ton.tl.constructors.readVectorTl
import org.ton.tl.constructors.writeIntTl
import org.ton.tl.constructors.writeVectorTl

@Serializable
data class AdnlAddressList(
        val addrs: List<AdnlAddress>,
        val version: Int,
        @SerialName("reinit_date")
        val reinitDate: Int,
        val priority: Int,
        @SerialName("expire_at")
        val expireAt: Int
) : Iterable<AdnlAddress> by addrs {
    companion object : TlConstructor<AdnlAddressList>(
            type = AdnlAddressList::class,
            schema = "adnl.addressList addrs:(vector adnl.Address) version:int reinit_date:int priority:int expire_at:int = adnl.AddressList"
    ) {
        override fun encode(output: Output, value: AdnlAddressList) {
            output.writeVectorTl(value.addrs, AdnlAddress)
            output.writeIntTl(value.version)
            output.writeIntTl(value.reinitDate)
            output.writeIntTl(value.priority)
            output.writeIntTl(value.expireAt)
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