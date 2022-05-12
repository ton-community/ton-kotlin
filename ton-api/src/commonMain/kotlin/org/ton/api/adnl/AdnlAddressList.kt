package org.ton.api.adnl

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.tl.TlConstructor

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
        override fun encode(output: Output, message: AdnlAddressList) {
            output.writeVector(message.addrs, AdnlAddress)
            output.writeIntLittleEndian(message.version)
            output.writeIntLittleEndian(message.reinitDate)
            output.writeIntLittleEndian(message.priority)
            output.writeIntLittleEndian(message.expireAt)
        }

        override fun decode(input: Input): AdnlAddressList {
            val addrs = input.readVector(AdnlAddress)
            val version = input.readIntLittleEndian()
            val reinitDate = input.readIntLittleEndian()
            val priority = input.readIntLittleEndian()
            val expireAt = input.readIntLittleEndian()
            return AdnlAddressList(addrs, version, reinitDate, priority, expireAt)
        }
    }
}