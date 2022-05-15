package org.ton.api.config

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.adnl.config.AdnlConfigGlobal
import org.ton.api.dht.config.DhtConfigGlobal
import org.ton.api.validator.config.ValidatorConfigGlobal
import org.ton.tl.TlConstructor

@SerialName("config.global")
@Serializable
data class ConfigGlobal(
    val adnl: AdnlConfigGlobal,
    val dht: DhtConfigGlobal,
    val validator: ValidatorConfigGlobal
) {
    companion object : TlConstructor<ConfigGlobal>(
        type = ConfigGlobal::class,
        schema = "config.global adnl:adnl.config.global dht:dht.config.global validator:validator.config.global = config.Global"
    ) {
        override fun encode(output: Output, message: ConfigGlobal) {
            output.writeTl(message.adnl, AdnlConfigGlobal)
            output.writeTl(message.dht, DhtConfigGlobal)
            output.writeTl(message.validator, ValidatorConfigGlobal)
        }

        override fun decode(input: Input): ConfigGlobal {
            val adnl = input.readTl(AdnlConfigGlobal)
            val dht = input.readTl(DhtConfigGlobal)
            val validator = input.readTl(ValidatorConfigGlobal)
            return ConfigGlobal(adnl, dht, validator)
        }
    }
}